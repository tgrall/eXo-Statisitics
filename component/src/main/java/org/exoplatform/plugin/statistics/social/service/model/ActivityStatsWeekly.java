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

import java.util.Collection;
import org.chromattic.api.annotations.Id;
import org.chromattic.api.annotations.Name;
import org.chromattic.api.annotations.OneToMany;
import org.chromattic.api.annotations.Path;
import org.chromattic.api.annotations.PrimaryType;

/**
 *
 * @author tgrall
 */
@PrimaryType(name = "exo:activityStatsWeekly")
public abstract class ActivityStatsWeekly {

    @OneToMany
    public abstract Collection<SocialStatistic> getChildren();

    @Id
    public abstract String getId();

    @Path
    public abstract String getPath();

    @Name
    public abstract String getName();
}
