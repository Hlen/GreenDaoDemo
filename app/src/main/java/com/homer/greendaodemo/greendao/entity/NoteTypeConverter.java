package com.homer.greendaodemo.greendao.entity;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by Homer on 2016/7/14.
 */
public class NoteTypeConverter implements PropertyConverter<NoteType, String> {

    @Override
    public NoteType convertToEntityProperty(String databaseValue) {
        return NoteType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(NoteType entityProperty) {
        return entityProperty.name();
    }
}
