import {IDepartment} from "./Department";

export interface IAgent {
  readonly id: number;
  readonly name: string;
  readonly birthDate: Date;
  readonly languages: string[];
  // private AgentRankEnum rank;
  readonly codeName: string;
  readonly passwordHash: string;
  readonly department: IDepartment;
  readonly missionIds: number[];
  readonly reportIds: number[];
  readonly rank: Rank;
}

type Rank = "TRAINEE" | "JUNIOR" | "SENIOR" | "AGENT_IN_CHARGE";