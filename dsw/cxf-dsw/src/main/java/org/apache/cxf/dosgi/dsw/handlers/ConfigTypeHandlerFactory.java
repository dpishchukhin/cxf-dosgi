/** 
  * Licensed to the Apache Software Foundation (ASF) under one 
  * or more contributor license agreements. See the NOTICE file 
  * distributed with this work for additional information 
  * regarding copyright ownership. The ASF licenses this file 
  * to you under the Apache License, Version 2.0 (the 
  * "License"); you may not use this file except in compliance 
  * with the License. You may obtain a copy of the License at 
  * 
  * http://www.apache.org/licenses/LICENSE-2.0 
  * 
  * Unless required by applicable law or agreed to in writing, 
  * software distributed under the License is distributed on an 
  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY 
  * KIND, either express or implied. See the License for the 
  * specific language governing permissions and limitations 
  * under the License. 
  */
package org.apache.cxf.dosgi.dsw.handlers;

import java.util.Map;
import java.util.logging.Logger;

import org.apache.cxf.dosgi.dsw.Constants;
import org.apache.cxf.dosgi.dsw.OsgiUtils;
import org.apache.cxf.dosgi.dsw.service.CxfDistributionProvider;
import org.osgi.framework.BundleContext;
import org.osgi.service.discovery.ServiceEndpointDescription;

public final class ConfigTypeHandlerFactory {
    
    private static final Logger LOG = Logger.getLogger(ConfigTypeHandlerFactory.class.getName());
    private static final ConfigTypeHandlerFactory FACTORY = new ConfigTypeHandlerFactory();
    
    private ConfigTypeHandlerFactory() {}
    
    public static ConfigTypeHandlerFactory getInstance() {
        return FACTORY;
    }
    
    public ConfigurationTypeHandler getHandler(BundleContext dswBC, ServiceEndpointDescription sd, 
                                               CxfDistributionProvider dp,
                                               Map<String, Object> handlerProperties) {
        String type = OsgiUtils.getProperty(sd, Constants.CONFIG_TYPE_PROPERTY);
        if (type == null || Constants.POJO_CONFIG_TYPE.equalsIgnoreCase(type)) {
            if (type == null) {
                LOG.info("Defaulting to pojo configuration type ");
            }
            
            if (OsgiUtils.getProperty(sd, Constants.POJO_HTTP_SERVICE_CONTEXT) != null) {
                if (OsgiUtils.getProperty(sd, Constants.POJO_ADDRESS_PROPERTY) != null) {
                    
                    
                    return null;
                }
                
                return new HttpServiceConfigurationTypeHandler(dswBC, dp, handlerProperties);                
            } else {
                return new PojoConfigurationTypeHandler(dswBC, dp, handlerProperties);
            }
        } else if (Constants.WSDL_CONFIG_TYPE.equalsIgnoreCase(type)) {
            return new WsdlConfigurationTypeHandler(dswBC, dp, handlerProperties);
        }
        
        LOG.info("Configuration type " + type + " is not supported");
        
        return null;
    }
}