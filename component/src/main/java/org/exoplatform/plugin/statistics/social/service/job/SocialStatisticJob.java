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
package org.exoplatform.plugin.statistics.social.service.job;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.quartz.Job;

import org.exoplatform.plugin.statistics.social.service.SocialStatisticService;

/**
 *
 * @author tgrall
 */
public abstract class SocialStatisticJob implements Job {
    
    protected SocialStatisticService getStatisticService() {
      ExoContainer containerContext = ExoContainerContext.getCurrentContainer();
      return (SocialStatisticService)containerContext.getComponentInstanceOfType(SocialStatisticService.class);        
    }    
    
}
