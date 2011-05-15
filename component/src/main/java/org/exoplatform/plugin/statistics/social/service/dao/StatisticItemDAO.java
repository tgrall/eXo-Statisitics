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
package org.exoplatform.plugin.statistics.social.service.dao;

import java.util.Date;

/**
 *
 * @author tgrall
 */
public class StatisticItemDAO {
    
    private Date startDate;
    private Date endDate;
    private long startDateTimeStamp;
    private long endDateTimeStamp;
    private long year;
    private long id;
    private String type;
    private long value;
    private long elapsedTime;
    private Date executionDate;

    public long getEndDateTimeStamp() {
        return endDateTimeStamp;
    }

    public void setEndDateTimeStamp(long endDateTimeStamp) {
        this.endDateTimeStamp = endDateTimeStamp;
    }

    public long getStartDateTimeStamp() {
        return startDateTimeStamp;
    }

    public void setStartDateTimeStamp(long startDateTimeStamp) {
        this.startDateTimeStamp = startDateTimeStamp;
    }


    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }
    
    
    public String toString() {
        StringBuilder buff = new StringBuilder();
        
        buff.append("[startdate : ").append(this.getStartDate()).append("]");
        buff.append("[enddate : ").append(this.getEndDate()).append("]");
        buff.append("[value : ").append(this.getValue()).append("]");
        buff.append("[year : ").append(this.getYear()).append("]");
        buff.append("[id : ").append(this.getId()).append("]");
        buff.append("[Elasped Time : ").append(this.getElapsedTime()).append("]");
        buff.append("[Exec Date : ").append(this.getExecutionDate()).append("]");
        
        return buff.toString();
    }
}
