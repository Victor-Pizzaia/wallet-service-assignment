package br.com.victorpizzaia.wallet_service_assignment.shared.domain;

import java.util.List;
import java.util.Map;

public record ValidationErrorResponse(String message, List<Map.Entry<String, String>> errors) {


}