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
package org.exoplatform.plugin.statistics.social.service.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Helper class to manage interval information based on basic date data
 * @author tgrall
 */
public class StatisticInterval {

    public final static int TYPE_WEEK = 0;
    public final static int TYPE_MONTH = 1;
    private int year = -1;
    private int id = -1;
    private int intervalType = 0;
    private Date dateToEvaluate = null;
    private Date startDate = null;
    private Date endDate = null;

    public StatisticInterval(Date date, int type) {
        dateToEvaluate = date;
        intervalType = type;

        this.setIntervalDates();


    }

    private void setIntervalDates() {

        Calendar cal = Calendar.getInstance();

        //TODO: Refactor in a generic method

        if (intervalType == TYPE_WEEK) {
            // take the date and find the first day for it
            cal.setTime(dateToEvaluate);
            int weekNumber = cal.get(Calendar.WEEK_OF_YEAR);
            int year = cal.get(Calendar.YEAR);
            if (this.year == -1) {
                this.year = year;
            }

            this.id = weekNumber;


            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.WEEK_OF_YEAR, weekNumber);
            cal.set(Calendar.DAY_OF_WEEK, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            startDate = cal.getTime();

            // calculate the last day of the week for this let's just
            // add a new week and remove 1 ms.
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            cal.add(Calendar.MILLISECOND, -1);

            endDate = cal.getTime();

        } else if (intervalType == TYPE_MONTH) {
            cal.setTime(dateToEvaluate);
            int monthNumber = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            if (this.year == -1) {
                this.year = year;
            }
            this.id = monthNumber;

            //TODO: Refactor in a generic method
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthNumber);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            startDate = cal.getTime();

            // calculate the last day of the week for this let's just
            // add a new week and remove 1 ms.
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.MILLISECOND, -1);
            endDate = cal.getTime();

        }
    }

    public StatisticInterval(int year, int id, int type) {
        if (this.year == -1) {
            this.year = year;
        }
        if (this.id == -1) {
            this.id = id;
        }
        this.intervalType = type;

        Calendar cal = Calendar.getInstance();

        // create the date to evaluate base on the information received
        if (intervalType == TYPE_MONTH) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, id);
            cal.set(year, id, 1, 0, 0, 0); // set the date to the first day of the month
            cal.set(Calendar.MILLISECOND, 0); // put all the time to 0
            startDate = cal.getTime();
            dateToEvaluate = cal.getTime();
        } else if (intervalType == TYPE_WEEK) {

            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.WEEK_OF_YEAR, id);
            cal.set(Calendar.DAY_OF_WEEK, 1);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            startDate = cal.getTime();
            dateToEvaluate = cal.getTime();
        }

        setIntervalDates();
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
