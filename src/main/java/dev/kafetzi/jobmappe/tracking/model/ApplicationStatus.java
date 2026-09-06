package dev.kafetzi.jobmappe.tracking.model;

public enum ApplicationStatus {
    // laufend — auf dem Board
    IN_PROGRESS,   // In Bearbeitung
    APPLIED,       // Beworben
    SCREENING,     // Vorauswahl
    INTERVIEW,     // Gespräch
    OFFER,         // Zusage erhalten

    // abgeschlossen — im Behälter
    ACCEPTED,      // Zusage angenommen
    DECLINED,      // Zusage abgelehnt
    REJECTED,      // Absage
    WITHDRAWN,     // Zurückgezogen
    NO_RESPONSE    // Keine Rückmeldung
}
