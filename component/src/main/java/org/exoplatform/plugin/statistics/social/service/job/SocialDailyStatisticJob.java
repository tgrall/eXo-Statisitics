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

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class SocialDailyStatisticJob extends SocialStatisticJob {
    
    
    private static final Log log = ExoLogger.getLogger(SocialDailyStatisticJob.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
      log.info("---- Daily Job ---- ");
      this.getStatisticService().calculateDailyStatistics();
    }
    
    
}
