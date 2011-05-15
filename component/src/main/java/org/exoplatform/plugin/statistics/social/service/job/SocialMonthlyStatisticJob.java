/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exoplatform.plugin.statistics.social.service.job;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 *
 * @author tgrall
 */
public class SocialMonthlyStatisticJob extends SocialStatisticJob {
    
    private static final Log log = ExoLogger.getLogger(SocialMonthlyStatisticJob.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
      log.info("---- Monthly Job ---- ");
      this.getStatisticService().calculateMonthlyStatistics();
    }
    
}
