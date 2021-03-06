package com.msws.shareplates.biz.share.entity;

import com.msws.shareplates.common.code.ScreenTypeCode;
import com.msws.shareplates.common.data.domain.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "share_user_socket")
public class ShareUserSocket extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_user_id")
    private ShareUser shareUser;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "focus_yn")
    private Boolean focusYn;

    @Column(name="screen_type_code", columnDefinition = "VARCHAR(15)")
    @Enumerated(EnumType.STRING)
    private ScreenTypeCode screenTypeCode;
}
