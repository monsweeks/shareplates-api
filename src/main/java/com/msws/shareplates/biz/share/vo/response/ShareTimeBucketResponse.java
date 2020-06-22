package com.msws.shareplates.biz.share.vo.response;

import com.msws.shareplates.biz.share.entity.ShareTimeBucket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShareTimeBucketResponse extends RepresentationModel<ShareTimeBucketResponse> {

    private Long id;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;

    public ShareTimeBucketResponse(ShareTimeBucket shareTimeBucket) {
        this.id = shareTimeBucket.getId();
        this.openDate = shareTimeBucket.getOpenDate();
        this.closeDate = shareTimeBucket.getCloseDate();
    }
}
