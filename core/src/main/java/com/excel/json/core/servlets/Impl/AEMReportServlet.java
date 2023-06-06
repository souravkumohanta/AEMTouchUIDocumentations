package com.excel.json.core.servlets.Impl;

import com.excel.json.core.service.ComponentDefinition;
import com.excel.json.core.service.ComponentDefinition;
import com.excel.json.core.service.Impl.ComponentDefinitionImpl;
import org.apache.jackrabbit.oak.commons.json.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;



import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

    @Component(
            service = Servlet.class,
            property = {
                    "sling.servlet.methods=GET",
                    "sling.servlet.paths=/bin/componentReport"
            }
    )
    public class AEMReportServlet extends SlingSafeMethodsServlet {
        @Reference
        private ResourceResolverFactory resolverFactory;
        @Reference
        ComponentDefinition componentDefinition ;

        @Reference
        ComponentDefinitionImpl componentDefinitionimpl;
        @Override
        protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter writer = response.getWriter();
            List<ComponentDefinition> componentDefinitions = componentDefinitionimpl.getComponentDefinitions(resolverFactory);


            // Generate JSON report
            String jsonReport = generateJsonReport(componentDefinitions);



            // Set the Content-Disposition header to prompt the user to download the report
            response.setHeader("Content-Disposition", "attachment; filename=\"component-report.json\"");

            // Write the JSON report to the response
            writer.print(jsonReport);
        }
        private String generateJsonReport(List<ComponentDefinition> componentDefinitions) {
            // Generate the JSON report using the component definitions
            // You can use a JSON library like Jackson to build the JSON structure

            // Example JSON report generation
            StringBuilder jsonReport = new StringBuilder();
            jsonReport.append("{\n");
            jsonReport.append("  \"components\": [\n");

            for (int i = 0; i < componentDefinitions.size(); i++) {
                ComponentDefinition componentDefinition = componentDefinitions.get(i);
                jsonReport.append("    {\n");
                jsonReport.append("      \"title\": \"").append(componentDefinition.getTitle()).append("\",\n");
                jsonReport.append("      \"description\": \"").append(componentDefinition.getDescription()).append("\",\n");
                jsonReport.append("      \"inputType\": \"").append(componentDefinition.getInputType()).append("\",\n");
                jsonReport.append("      \"mandatory\": ").append(componentDefinition.isMandatory()).append("\n");
                jsonReport.append("    }");

                if (i < componentDefinitions.size() - 1) {
                    jsonReport.append(",");
                }

                jsonReport.append("\n");
            }

            jsonReport.append("  ]\n");
            jsonReport.append("}");

            return jsonReport.toString();
        }
        private void generateJsonReport(List<ComponentDefinition> componentDefinitions, HttpServletResponse response) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonReport = objectMapper.createObjectNode();

            ArrayNode componentArray = jsonReport.putArray("components");
            for (ComponentDefinition componentDefinition : componentDefinitions) {
                ObjectNode componentNode = componentArray.addObject();
                componentNode.put("name", componentDefinition.getName());
                componentNode.put("resourceType", componentDefinition.getResourceType());
                componentNode.put("description", componentDefinition.getDescription());
                // Add more properties here once testing done


                componentNode.put("fieldDescription", componentDefinition.getFieldDescription());
                componentNode.put("label", componentDefinition.getLabel());
                componentNode.put("inputType", componentDefinition.getInputType());
                componentNode.put("isHidden", componentDefinition.isHidden());
                componentNode.put("isMandatory", componentDefinition.isMandatory());
                componentNode.put("showHideCondition", componentDefinition.getShowHideCondition());
            }

            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonReport);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }



    }

