package cz.fi.muni.pa165.secretagency.controllers;

import cz.fi.muni.pa165.secretagency.ApiUris;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Adam Kral <433328>
 * Date: 12/11/18
 * Time: 4:29 PM
 */
@RestController
public class MainController {
    /**
     * Copied from seminar
     * The main entry point of the REST API
     * Provides access to all the resources with links to resource URIs
     * Can be even extended further so that all the actions on all the resources are available
     * and can be reused in all the controllers (possibly in full HATEOAS style)
     *
     * @return resources uris
     */
    @RequestMapping(value = "rest/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final Map<String, String> getResources() {

        Map<String,String> resourcesMap = new HashMap<>();
        resourcesMap.put("departments_uri", ApiUris.ROOT_URI_DEPARTMENTS);
        return Collections.unmodifiableMap(resourcesMap);

    }
}
