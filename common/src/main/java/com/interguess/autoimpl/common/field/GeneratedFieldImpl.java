package com.interguess.autoimpl.common.field;

import com.interguess.autoimpl.api.field.GeneratedField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.lang.model.type.TypeMirror;

@Getter
@Builder
@AllArgsConstructor
public class GeneratedFieldImpl implements GeneratedField {

    private final TypeMirror type;

    private final String name;
}
