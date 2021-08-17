package org.hifly.service;

import java.util.Collection;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.model.ProcessInstanceDesc;
import org.kie.internal.query.QueryContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(value = "Additional Endpoints", produces = MediaType.APPLICATION_JSON)
@Path("pam")
@RestController
@RequestMapping("/processinstance")
public class ProcessInstanceController {

    @Autowired
    private RuntimeDataService runtimeDataService;

    @Autowired
    private ProcessService processService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<ProcessInstanceDesc> getProcessInstances() {

        Collection<ProcessInstanceDesc> processInstances = runtimeDataService.getProcessInstances(new QueryContext(0, 100, "status", true));

        return processInstances;
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public ProcessInstanceDesc getProcessInstance(@RequestParam String id) {
        long processInstanceId = Long.parseLong(id);
        ProcessInstanceDesc processInstance = runtimeDataService.getProcessInstanceById(processInstanceId);

        return processInstance;
    }

    @RequestMapping(value = "/abort", method = RequestMethod.POST)
    public String abortProcessInstance(@RequestParam String id) {

        processService.abortProcessInstance(Long.parseLong(id));

        return "Instance (" + id + ") aborted successfully";
    }

    @RequestMapping(value = "/signal", method = RequestMethod.POST)
    public String signalProcessInstance(@RequestParam String id, @RequestParam String signal,
                                        @RequestParam String data) {

        processService.signalProcessInstance(Long.parseLong(id), signal, data);

        return "Signal sent to instance (" + id + ") successfully";
    }

    @RequestMapping(value = "/availableSignals", method = RequestMethod.POST)
    public Collection<String> availableSignals(@RequestParam String id) {

        Collection<String> availableSignals = processService.getAvailableSignals(Long.parseLong(id));

        return availableSignals;
    }

}
