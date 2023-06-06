package com.excel.json.core.service.Impl;
import com.excel.json.core.service.ComponentDefinition;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class ComponentDefinitionImpl extends ComponentDefinition {
    public List<ComponentDefinition> getComponentDefinitions(ResourceResolverFactory resolverFactory) {

        List<ComponentDefinition> componentDefinitions = new ArrayList<>();

        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = resolverFactory.getServiceResourceResolver(null);

            Resource appsFolder = resourceResolver.getResource("/apps");
            if (appsFolder != null) {
                Iterator<Resource> componentResources = appsFolder.listChildren();

                while (componentResources.hasNext()) {

                    Resource componentResource = componentResources.next();
                    Node componentNode = componentResource.adaptTo(Node.class);
                    if (componentNode != null) {
                        try {
                            String componentName = componentNode.getName();
                            String fieldLabel = componentNode.hasProperty("fieldLabel") ? componentNode.getProperty("fieldLabel").getString() : "";
                            String componentTitle = componentNode.getProperty("jcr:title").getString();
                            String componentDescription = componentNode.getProperty("jcr:description").getString();
                            String resourceSupertype = componentNode.getProperty("sling:resourceSuperType").getString();

                            // Get the input type property from a specific node
                            Node componentPropertiesNode = componentNode.getNode("cq:dialog");
                            String inputType = "";
                            if (componentPropertiesNode.hasProperty("properties")) {
                                Node propertiesNode = componentPropertiesNode.getNode("properties");
                                if (propertiesNode.hasProperty("inputType")) {
                                    inputType = propertiesNode.getProperty("inputType").getString();
                                }
                            }

                            // Check if the component has variations
                            boolean hasVariations = false;
                            if (componentNode.hasNode("cq:templates")) {
                                Node templatesNode = componentNode.getNode("cq:templates");
                                hasVariations = templatesNode.hasNodes();
                            }

                            // Get the list of required fields
                            List<String> requiredFields = getRequiredFields(componentNode);

                            // Check if the field is hidden
                            boolean isHidden = false;
                            if (componentNode.hasProperty("xtype") && componentNode.getProperty("xtype").getString().equals("hidden")) {
                                isHidden = true;
                            }

                            // Check if the field has showhide condition or logic
                            boolean hasShowHideLogic = false;
                            if (componentNode.hasProperty("showhidetarget")) {
                                hasShowHideLogic = true;
                            }

                            // Add the component definition to the list
                            ComponentDefinition componentDefinition = new ComponentDefinition();
                            componentDefinition.setName(componentName);
                            componentDefinition.setTitle(componentTitle);
                            componentDefinition.setDescription(componentDescription);
                            componentDefinition.setResourceSupertype(resourceSupertype);
                            componentDefinition.setInputType(inputType);
                            componentDefinition.setHasVariations(hasVariations);
                            componentDefinition.setRequiredFields(requiredFields);
                            componentDefinition.setLabel(fieldLabel);
                            componentDefinition.setIsHidden(isHidden);
                            componentDefinition.setHasShowHideLogic(hasShowHideLogic);


                            componentDefinitions.add(componentDefinition);
                        } catch (RepositoryException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }

        return componentDefinitions;
    }



    private List<String> getRequiredFields(Node componentNode) throws RepositoryException {
        List<String> requiredFields = new ArrayList<>();
        if (componentNode.hasNode("cq:dialog")) {
            Node dialogNode = componentNode.getNode("cq:dialog");
            if (dialogNode.hasNode("content")) {
                Node contentNode = dialogNode.getNode("content");
                NodeIterator fieldNodes = contentNode.getNodes();
                while (fieldNodes.hasNext()) {
                    Node fieldNode = fieldNodes.nextNode();
                    if (fieldNode.hasProperty("required") && fieldNode.getProperty("required").getBoolean()) {
                        requiredFields.add(fieldNode.getName());
                    }
                }
            }
        }
        return requiredFields;
    }

}

