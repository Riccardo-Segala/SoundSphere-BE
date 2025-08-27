package backend.dto.filiale;

public record BranchAddressDTO(
        String via,
        String citta,
        String cap,
        String provincia,
        String nazione
)
{}
