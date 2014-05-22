/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.db;

import com.google.common.collect.Maps;
import org.sonar.api.ServerComponent;
import org.sonar.core.persistence.DaoComponent;
import org.sonar.core.persistence.Database;
import org.sonar.core.persistence.DbSession;
import org.sonar.core.persistence.MyBatis;
import org.sonar.core.properties.PropertiesDao;
import org.sonar.core.qualityprofile.db.QualityProfileDao;
import org.sonar.core.resource.ResourceDao;
import org.sonar.core.technicaldebt.db.CharacteristicDao;
import org.sonar.core.template.LoadedTemplateDao;
import org.sonar.server.component.persistence.ComponentDao;
import org.sonar.server.measure.persistence.MeasureDao;
import org.sonar.server.qualityprofile.persistence.ActiveRuleDao;
import org.sonar.server.rule2.persistence.RuleDao;

import java.util.Map;

/**
 * Facade for all db components, mainly DAOs
 */
public class DbClient implements ServerComponent {

  private final Database db;
  private final MyBatis myBatis;
  private final RuleDao ruleDao;
  private final ActiveRuleDao activeRuleDao;
  private final QualityProfileDao qualityProfileDao;
  private final CharacteristicDao debtCharacteristicDao;
  private final LoadedTemplateDao loadedTemplateDao;
  private final PropertiesDao propertiesDao;
  private final ComponentDao componentDao;
  private final ResourceDao resourceDao;
  private final MeasureDao measureDao;

  public DbClient(Database db, MyBatis myBatis, DaoComponent... daoComponents) {
    this.db = db;
    this.myBatis = myBatis;

    Map<Class, DaoComponent> map = Maps.newHashMap();
    for (DaoComponent daoComponent : daoComponents) {
      map.put(daoComponent.getClass(), daoComponent);
    }
    ruleDao = getDao(map, RuleDao.class);
    activeRuleDao = getDao(map, ActiveRuleDao.class);
    debtCharacteristicDao = getDao(map, CharacteristicDao.class);
    qualityProfileDao = getDao(map, QualityProfileDao.class);
    loadedTemplateDao = getDao(map, LoadedTemplateDao.class);
    propertiesDao = getDao(map, PropertiesDao.class);
    componentDao = getDao(map, ComponentDao.class);
    resourceDao = getDao(map, ResourceDao.class);
    measureDao = getDao(map, MeasureDao.class);
  }

  public Database database() {
    return db;
  }

  public DbSession openSession(boolean batch) {
    return myBatis.openSession(batch);
  }

  public RuleDao ruleDao() {
    return ruleDao;
  }

  public ActiveRuleDao activeRuleDao() {
    return activeRuleDao;
  }

  public QualityProfileDao qualityProfileDao() {
    return qualityProfileDao;
  }

  public CharacteristicDao debtCharacteristicDao() {
    return debtCharacteristicDao;
  }

  public LoadedTemplateDao loadedTemplateDao() {
    return loadedTemplateDao;
  }

  public PropertiesDao propertiesDao() {
    return propertiesDao;
  }

  public ComponentDao componentDao() {
    return componentDao;
  }

  public ResourceDao resourceDao() {
    return resourceDao;
  }

  public MeasureDao measureDao() {
    return measureDao;
  }

  private <K> K getDao(Map<Class, DaoComponent> map, Class<K> clazz) {
    return (K) map.get(clazz);
  }
}
