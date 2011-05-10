/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *
 */
package org.exoplatform.plugin.statistics.social.service.model.integration;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jcr.Credentials;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.chromattic.spi.jcr.SessionLifeCycle;


import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.lang.reflect.Method;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.config.RepositoryConfigurationException;


/**
 *
 * @author tgrall
 */
public class CurrentRepositoryLifeCycle implements SessionLifeCycle{
    
   private Repository getCurrentRepository() throws RepositoryException {
       
      ExoContainer containerContext = ExoContainerContext.getCurrentContainer();
      RepositoryService repoService = (RepositoryService)containerContext.getComponentInstanceOfType(RepositoryService.class) ;
        try {
            return repoService.getDefaultRepository();
        } catch (RepositoryConfigurationException ex) {
            Logger.getLogger(CurrentRepositoryLifeCycle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
       
   }
    
   public Session login() throws RepositoryException
   {
      Repository repo = getCurrentRepository();
      return repo.login();
   }

   public Session login(String workspace) throws RepositoryException
   {
      Repository repo = getCurrentRepository();
      return repo.login(workspace);
   }

   public Session login(Credentials credentials, String workspace) throws RepositoryException
   {
      Repository repo = getCurrentRepository();
      return repo.login(credentials, workspace);
   }

   public Session login(Credentials credentials) throws RepositoryException
   {
      Repository repo = getCurrentRepository();
      return repo.login(credentials);
   }

   public void save(Session session) throws RepositoryException
   {
      session.save();
   }

   public void close(Session session)
   {
      session.logout();
   }    
    
}
