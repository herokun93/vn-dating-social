package vn.dating.app.social.repositories.ennum;

import vn.dating.app.social.models.eenum.PostStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PostStatusConverter implements AttributeConverter<PostStatus, String> {

    @Override
    public String convertToDatabaseColumn(PostStatus attribute) {
        return attribute.toString();
    }

    @Override
    public PostStatus convertToEntityAttribute(String dbData) {
        return PostStatus.valueOf(dbData);
    }
}
