package mh.cyb.root.DpiBatchMeetBackend.modules.event.mapper;

import mh.cyb.root.DpiBatchMeetBackend.modules.event.domain.Registration;
import mh.cyb.root.DpiBatchMeetBackend.modules.event.dto.RegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.title", target = "eventTitle")
    RegistrationDto toDto(Registration registration);
}
