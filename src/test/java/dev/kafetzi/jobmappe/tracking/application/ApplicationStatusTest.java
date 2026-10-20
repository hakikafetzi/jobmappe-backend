package dev.kafetzi.jobmappe.tracking.application;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ApplicationStatusTest {

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"OFFER_ACCEPTED", "OFFER_REJECTED", "REJECTED", "WITHDRAWN", "NO_RESPONSE"}
    )
    void isClosed_finalStatus_returnsTrue(ApplicationStatus status) {
        assertThat(status.isClosed()).isTrue();
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"OFFER_ACCEPTED", "OFFER_REJECTED", "REJECTED", "WITHDRAWN", "NO_RESPONSE"},
            mode = EnumSource.Mode.EXCLUDE
    )
    void isClosed_openStatus_returnsFalse(ApplicationStatus status) {
        assertThat(status.isClosed()).isFalse();
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"APPLIED", "WITHDRAWN"}
    )
    void canTransitionTo_fromInProgressToAllowedStatus_returnsTrue(ApplicationStatus target) {
        assertThat(ApplicationStatus.IN_PROGRESS.canTransitionTo(target)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"APPLIED", "WITHDRAWN"},
            mode = EnumSource.Mode.EXCLUDE
    )
    void canTransitionTo_fromInProgressToOtherStatus_returnsFalse(ApplicationStatus target) {
        assertThat(ApplicationStatus.IN_PROGRESS.canTransitionTo(target)).isFalse();
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"IN_PROGRESS", "WAITING", "INTERVIEW", "OFFER", "REJECTED", "NO_RESPONSE", "WITHDRAWN"}
    )
    void canTransitionTo_fromAppliedToAllowedStatus_returnsTrue(ApplicationStatus target) {
        assertThat(ApplicationStatus.APPLIED.canTransitionTo(target)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"IN_PROGRESS", "WAITING", "INTERVIEW", "OFFER", "REJECTED", "NO_RESPONSE", "WITHDRAWN"},
            mode = EnumSource.Mode.EXCLUDE
    )
    void canTransitionTo_fromAppliedToOtherStatus_returnsFalse(ApplicationStatus target) {
        assertThat(ApplicationStatus.APPLIED.canTransitionTo(target)).isFalse();
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"IN_PROGRESS", "APPLIED", "WAITING", "OFFER", "REJECTED", "NO_RESPONSE", "WITHDRAWN"}
    )
    void canTransitionTo_fromInterviewToAllowedStatus_returnsTrue(ApplicationStatus target) {
        assertThat(ApplicationStatus.INTERVIEW.canTransitionTo(target)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"IN_PROGRESS", "APPLIED", "WAITING", "OFFER", "REJECTED", "NO_RESPONSE", "WITHDRAWN"},
            mode = EnumSource.Mode.EXCLUDE
    )
    void canTransitionTo_fromInterviewToOtherStatus_returnsFalse(ApplicationStatus target) {
        assertThat(ApplicationStatus.INTERVIEW.canTransitionTo(target)).isFalse();
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"IN_PROGRESS", "APPLIED", "INTERVIEW", "OFFER", "REJECTED", "NO_RESPONSE", "WITHDRAWN"}
    )
    void canTransitionTo_fromWaitingToAllowedStatus_returnsTrue(ApplicationStatus target) {
        assertThat(ApplicationStatus.WAITING.canTransitionTo(target)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"IN_PROGRESS", "APPLIED", "INTERVIEW", "OFFER", "REJECTED", "NO_RESPONSE", "WITHDRAWN"},
            mode = EnumSource.Mode.EXCLUDE
    )
    void canTransitionTo_fromWaitingToOtherStatus_returnsFalse(ApplicationStatus target) {
        assertThat(ApplicationStatus.WAITING.canTransitionTo(target)).isFalse();
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"IN_PROGRESS", "APPLIED", "WAITING", "INTERVIEW", "OFFER_ACCEPTED", "OFFER_REJECTED"}
    )
    void canTransitionTo_fromOfferToAllowedStatus_returnsTrue(ApplicationStatus target) {
        assertThat(ApplicationStatus.OFFER.canTransitionTo(target)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"IN_PROGRESS", "APPLIED", "WAITING", "INTERVIEW", "OFFER_ACCEPTED", "OFFER_REJECTED"},
            mode = EnumSource.Mode.EXCLUDE
    )
    void canTransitionTo_fromOfferToOtherStatus_returnsFalse(ApplicationStatus target) {
        assertThat(ApplicationStatus.OFFER.canTransitionTo(target)).isFalse();
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"OFFER_ACCEPTED", "OFFER_REJECTED", "REJECTED", "WITHDRAWN", "NO_RESPONSE"}
    )
    void canTransitionTo_fromFinalStatusToReopenTargets_returnsTrue(ApplicationStatus from) {
        assertThat(List.of(ApplicationStatus.IN_PROGRESS, ApplicationStatus.APPLIED, ApplicationStatus.WAITING,
                        ApplicationStatus.INTERVIEW, ApplicationStatus.OFFER)).allMatch(from::canTransitionTo);
    }

    @ParameterizedTest
    @EnumSource(
            value = ApplicationStatus.class,
            names = {"OFFER_ACCEPTED", "OFFER_REJECTED", "REJECTED", "WITHDRAWN", "NO_RESPONSE"}
    )
    void canTransitionTo_fromFinalStatusToOtherTargets_returnsFalse(ApplicationStatus from) {
        assertThat(List.of(ApplicationStatus.OFFER_ACCEPTED, ApplicationStatus.OFFER_REJECTED,
                ApplicationStatus.REJECTED, ApplicationStatus.WITHDRAWN, ApplicationStatus.NO_RESPONSE))
                .noneMatch(from::canTransitionTo);
    }
}
