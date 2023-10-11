package com.crude.travelcrew.domain.record.model.dto;

import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.record.model.entity.Record;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordHeartRes {

    private Long record_heart_id;
    private Member member;
    private Record record;

}
