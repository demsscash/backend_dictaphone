package com.personnel.personnelservice.config;

import com.personnel.personnelservice.core.models.enums.PermissionEnum;
import com.personnel.personnelservice.core.models.enums.RoleEnum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe de configuration définissant les permissions associées à chaque rôle.
 */
public class RolePermissionsConfig {

    /**
     * Retourne une map contenant les rôles et leurs permissions associées.
     * @return Map<RoleEnum, EnumSet<PermissionEnum>> association des rôles avec leurs permissions
     */
    public static Map<RoleEnum, EnumSet<PermissionEnum>> getRolePermissionsMap() {
        Map<RoleEnum, EnumSet<PermissionEnum>> rolePermissionsMap = new HashMap<>();

        rolePermissionsMap.put(RoleEnum.PATIENT, EnumSet.of(
                PermissionEnum.VIEW_RAPPORT,
                PermissionEnum.APPOINTMENT_CREATE,
                PermissionEnum.VIEW_AGENDA,
                PermissionEnum.VIEW_DOSSIER,
                PermissionEnum.VIEW_IMAGERIE,
                PermissionEnum.VIEW_ORDONNANCES,
                PermissionEnum.VIEW_ANALYSES,
                PermissionEnum.VIEW_HEALTH_TABLE,
                PermissionEnum.VIEW_VISITED_CABINETS,
                PermissionEnum.VIDEO_CONSULTATION));

        rolePermissionsMap.put(RoleEnum.ASSISTANT, EnumSet.of(
                PermissionEnum.APPOINTMENT_CREATE,
                PermissionEnum.VIEW_PATIENTS));

        rolePermissionsMap.put(RoleEnum.MEDECIN, EnumSet.of(
                PermissionEnum.DOCTOR_READ,
                PermissionEnum.VIEW_AGENDA,
                PermissionEnum.MEDICAL_RECORD_CREATE,
                PermissionEnum.ASSISTANT_READ,
                PermissionEnum.PATIENT_READ,
                PermissionEnum.VIEW_PATIENTS,
                PermissionEnum.PRESCRIPTION_CREATE));

        return rolePermissionsMap;
    }
}