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

import java.util.Date;
import org.chromattic.api.annotations.Id;
import org.chromattic.api.annotations.Name;
import org.chromattic.api.annotations.Path;
import org.chromattic.api.annotations.PrimaryType;
import org.chromattic.api.annotations.Property;


/**
 *
 * @author tgrall
 */
@PrimaryType(name = "exo:statisticItem")
public abstract class SocialStatistic {

    public final static String FIELD_ID_IN_YEAR = "idInYear";
    public final static String FIELD_YEAR = "year";
    public final static String FIELD_START_DATE = "startDate";
    public final static String FIELD_END_DATE = "endDate";
    public final static String FIELD_VALUE_COUNT = "value";
    public final static String FIELD_EXECUTION_DATE = "executionDate";
    public final static String FIELD_ELAPSED_TIME = "elapsedTime";
    
    
   @Id
   public abstract String getId();

   @Path
   public abstract String getPath();

   @Name
   public abstract String getName();

 
    
   @Property(name = "executionDate")
   public abstract Date getExecutionDate();
   public abstract void setExecutionDate(Date executionDate);
    
   @Property(name = "startDate")
   public abstract Date getStartDate();
   public abstract void setStartDate(Date startDate);

   @Property(name = "endDate")
   public abstract Date getEndDate();
   public abstract void setEndDate(Date endDate);

   @Property(name = "type")
   public abstract String getType();
   public abstract void setType(String type);
   
   @Property(name = "year")
   public abstract int getYear();
   public abstract void setYear(int year);

   @Property(name = "idInYear")
   public abstract int getIdInYear();
   public abstract void setIdInYear(int idInYear);

   @Property(name = "value")
   public abstract long getValue();
   public abstract void setValue(long value);

   @Property(name = "elapsedTime")
   public abstract long getElapsedTime();
   public abstract void setElapsedTime(long elapsedTime);

}
