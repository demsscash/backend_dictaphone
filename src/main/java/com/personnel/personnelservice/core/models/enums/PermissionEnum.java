package com.personnel.personnelservice.core.models.enums;

public enum PermissionEnum {
    // Permissions utilisateur
    USER_CREATE("Créer un utilisateur"),
    USER_READ("Lire les informations utilisateur"),
    USER_UPDATE("Modifier un utilisateur"),
    USER_DELETE("Supprimer un utilisateur"),

    // Permissions Patient
    PATIENT_CREATE("Créer un patient"),
    PATIENT_READ("Voir les informations patient"),
    PATIENT_UPDATE("Modifier un patient"),
    PATIENT_DELETE("Supprimer un patient"),
    VIEW_PATIENTS("Voir la liste des patients"),

    // Permissions Médecin
    DOCTOR_CREATE("Créer un médecin"),
    DOCTOR_READ("Voir les informations médecin"),
    DOCTOR_UPDATE("Modifier un médecin"),
    DOCTOR_DELETE("Supprimer un médecin"),

    // Permissions Assistant
    ASSISTANT_CREATE("Créer un assistant"),
    ASSISTANT_READ("Voir les informations assistant"),
    ASSISTANT_UPDATE("Modifier un assistant"),
    ASSISTANT_DELETE("Supprimer un assistant"),

    // Permissions Personnel
    MANAGE_STAFF("Gérer les personnels"),

    // Permissions Rendez-vous
    APPOINTMENT_CREATE("Créer un rendez-vous"),
    APPOINTMENT_READ("Voir les rendez-vous"),
    APPOINTMENT_UPDATE("Modifier un rendez-vous"),
    APPOINTMENT_DELETE("Supprimer un rendez-vous"),
    MANAGE_APPOINTMENTS("Gérer les rendez-vous"),
    VIEW_AGENDA("Voir l'agenda"),
    VIEW_WAITING_ROOM("Voir la salle d'attente"),

    // Permissions Dossier Médical
    MEDICAL_RECORD_CREATE("Créer un dossier médical"),
    MEDICAL_RECORD_READ("Voir un dossier médical"),
    MEDICAL_RECORD_UPDATE("Modifier un dossier médical"),
    MEDICAL_RECORD_DELETE("Supprimer un dossier médical"),
    VIEW_DOSSIER("Voir le dossier électronique"),
    VIEW_IMAGERIE("Voir l'imagerie"),
    VIEW_ANALYSES("Voir les analyses"),
    VIEW_RAPPORT("Voir les rapports"),

    // Permissions Prescription
    PRESCRIPTION_CREATE("Créer une prescription"),
    PRESCRIPTION_READ("Voir une prescription"),
    PRESCRIPTION_UPDATE("Modifier une prescription"),
    PRESCRIPTION_DELETE("Supprimer une prescription"),
    VIEW_ORDONNANCES("Voir les ordonnances"),

    // Permissions Tableau de bord
    VIEW_DASHBOARD("Voir le tableau de bord"),

    // Permissions Cabinet
    VIEW_CABINET("Voir les informations du cabinet"),
    VIEW_MEDICAMENTS("Voir les médicaments"),
    VIEW_ACTES("Voir les actes"),
    VIEW_FACTURES("Voir les factures"),
    VIEW_ARCHIVE("Voir les archives"),

    // Permissions Vidéo consultation
    VIDEO_CONSULTATION("Utiliser la vidéo consultation"),

    VIEW_HEALTH_TABLE( "Voir la table de santé" ),
    VIEW_VISITED_CABINETS ("Voir les cabinets déjà visités par le patient ");


    private final String description;

    PermissionEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}