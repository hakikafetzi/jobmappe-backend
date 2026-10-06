package dev.kafetzi.jobmappe.tracking.application;

import java.util.EnumSet;

public enum ApplicationStatus {
    // laufend — auf dem Board
    IN_PROGRESS,   // In Bearbeitung
    APPLIED,       // Beworben
    WAITING,       // Warten
    INTERVIEW,     // Gespräch
    OFFER,         // Zusage erhalten

    // abgeschlossen — im Behälter
    OFFER_ACCEPTED, // Zusage angenommen
    OFFER_REJECTED, // Zusage abgelehnt
    REJECTED,       // Absage
    WITHDRAWN,      // Zurückgezogen
    NO_RESPONSE;     // Keine Rückmeldung

    public boolean canTransitionTo(ApplicationStatus newStatus) {
        return switch (this) {
            case IN_PROGRESS -> newStatus == APPLIED || newStatus == WITHDRAWN;
            case APPLIED ->
                    EnumSet.of(IN_PROGRESS, WAITING, INTERVIEW, OFFER, REJECTED, NO_RESPONSE, WITHDRAWN)
                            .contains(newStatus);
            case INTERVIEW ->
                    EnumSet.of(IN_PROGRESS, APPLIED, WAITING, OFFER, REJECTED, NO_RESPONSE, WITHDRAWN)
                            .contains(newStatus);
            case WAITING ->
                    EnumSet.of(IN_PROGRESS, APPLIED, INTERVIEW, OFFER, REJECTED, NO_RESPONSE, WITHDRAWN)
                            .contains(newStatus);
            case OFFER ->
                    EnumSet.of(IN_PROGRESS, APPLIED, WAITING, INTERVIEW, OFFER_ACCEPTED, OFFER_REJECTED)
                            .contains(newStatus);
            case OFFER_ACCEPTED, OFFER_REJECTED, REJECTED, WITHDRAWN, NO_RESPONSE ->
                    EnumSet.of(IN_PROGRESS, APPLIED, WAITING, INTERVIEW, OFFER)
                            .contains(newStatus);
        };
    }

    public boolean isClosed() {
        return switch (this) {
            case OFFER_ACCEPTED, OFFER_REJECTED, REJECTED, WITHDRAWN, NO_RESPONSE -> true;
            default -> false;
        };
    }
}
