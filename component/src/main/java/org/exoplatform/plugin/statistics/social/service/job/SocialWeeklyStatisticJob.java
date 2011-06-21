/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exoplatform.plugin.statistics.social.service.job;


import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.exoplatform.plugin.statistics.social.service.SocialStatisticService;


/**
 *
 * @author tgrall
 */
public class SocialWeeklyStatisticJob extends SocialStatisticJob {
    
    private static final Log log = ExoLogger.getLogger(SocialWeeklyStatisticJob.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
     log.info("---- Weekly Job  ---- ");
     this.getStatisticService().calculateWeeklyStatistics();        
    }
    


    
}
