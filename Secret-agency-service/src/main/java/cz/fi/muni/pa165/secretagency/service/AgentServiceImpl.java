package cz.fi.muni.pa165.secretagency.service;

import cz.fi.muni.pa165.secretagency.dao.AgentDao;
import cz.fi.muni.pa165.secretagency.entity.Agent;
import cz.fi.muni.pa165.secretagency.entity.Department;
import cz.fi.muni.pa165.secretagency.entity.Mission;
import cz.fi.muni.pa165.secretagency.enums.AgentRankEnum;
import cz.fi.muni.pa165.secretagency.enums.LanguageEnum;
import cz.fi.muni.pa165.secretagency.service.exceptions.AgentServiceException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the {@link AgentService}. This class is part of the
 * service module of the application that provides the implementation of the
 * business logic (main logic of the application).
 *
 * Author: Adam Kral <433328>
 * Date: 11/16/18
 * Time: 2:54 PM
 */
@Service
public class AgentServiceImpl extends GenericServiceImpl<Agent, AgentDao> implements AgentService {

    @Override
    public boolean authenticate(Agent agent, String password) {
        if (agent == null) {
            throw new NullPointerException("Agent must be set");
        }
        return validatePassword(password, agent.getPasswordHash());
    }

    @Override
    public boolean isAdmin(Agent agent) {
        if (agent == null) {
            throw new NullPointerException("Agent must be set");
        }
        return agent.getRank() == AgentRankEnum.AGENT_IN_CHARGE;
    }

    @Override
    public List<Agent> getAgentsByRank(AgentRankEnum rankEnum) {
        if (rankEnum == null) {
            throw new NullPointerException("Rank cannot be null");
        }
        return getDao().getAgentsWithRank(rankEnum);
    }

    @Override
    public Agent updateAgent(Agent agent) {
        if (agent == null) {
            throw new NullPointerException("agent cannot be null");
        }
        return getDao().merge(agent);
    }

    @Override
    public Agent getAgentByCodeName(String codename) {
        if (codename == null) {
            throw new NullPointerException("Code name cannot be null");
        }
        return getDao().getAgentByCodename(codename);
    }

    @Override
    public void assignAgentToMission(Agent agent, Mission mission) {
        if (agent == null) {
            throw new NullPointerException("Agent cannot be null");
        }

        if (mission == null) {
            throw new NullPointerException("Mission cannot be null");
        }

        if (agent.getMissions().contains(mission)) {
            throw new AgentServiceException("Cannot assign agent to mission, already assigned");
        }
        mission.addAgent(agent);
        getDao().merge(agent);
    }

    @Override
    public void removeAgentFromMission(Agent agent, Mission mission) {
        if (agent == null) {
            throw new NullPointerException("Agent cannot be null");
        }

        if (mission == null) {
            throw new NullPointerException("Mission cannot be null");
        }
        if (!agent.getMissions().contains(mission)) {
            throw new AgentServiceException("Cannot remove agent from mission, could not find mission");
        }
        mission.removeAgent(agent);
        getDao().merge(agent);
    }

    @Override
    public void addAgentToDepartment(Agent agent, Department department) {
        if (agent == null) {
            throw new NullPointerException("Agent cannot be null");
        }

        if (department == null) {
            throw new NullPointerException("Department cannot be null");
        }
        department.addAgent(agent);
        getDao().merge(agent);
    }

    @Override
    public List<Agent> getAgentsWithLanguageKnowledge(LanguageEnum language) {
        if (language == null) {
            throw new NullPointerException("Language cannot be null");
        }
        return getDao().getAgentsWithLanguageKnowledge(language);
    }

    @Override
    public List<Agent> getSoonRetiringAgents() {
        return getDao().getSoonRetiringAgents();
    }

    @Override
    public Set<Agent> getAgentsWithCodeNames(Set<String> codeNames) {
        if (codeNames == null) {
            throw new NullPointerException("Codenames must be set");
        }

        if (codeNames.isEmpty()) {
            return new HashSet<>();
        }

        return getDao().getAgentsWithCodeNames(codeNames);
    }

    // PRIVATE METHODS
    // validation of password - copied from seminar
    //see  https://crackstation.net/hashing-security.htm#javasourcecode
    public static String createHash(String password) {
        final int SALT_BYTE_SIZE = 24;
        final int HASH_BYTE_SIZE = 24;
        final int PBKDF2_ITERATIONS = 1000;
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);
        // Hash the password
        byte[] hash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        // format iterations:salt:hash
        return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean validatePassword(String password, String correctHash) {
        if(password==null) return false;
        if(correctHash==null) throw new IllegalArgumentException("password hash is null");
        String[] params = correctHash.split(":");
        int iterations = Integer.parseInt(params[0]);
        byte[] salt = fromHex(params[1]);
        byte[] hash = fromHex(params[2]);
        byte[] testHash = pbkdf2(password.toCharArray(), salt, iterations, hash.length);
        return slowEquals(hash, testHash);
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line
     * system using a timing attack and then attacked off-line.
     *
     * @param a the first byte array
     * @param b the second byte array
     * @return true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        return paddingLength > 0 ? String.format("%0" + paddingLength + "d", 0) + hex : hex;
    }
}
