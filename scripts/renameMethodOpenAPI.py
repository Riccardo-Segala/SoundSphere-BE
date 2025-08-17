import yaml
import re
import sys

# Mappatura esplicita da nomi italiani (endpoint) a nomi inglesi (entità)
ENTITY_MAPPING = {
    "vantaggio": "Benefit",
    "utenti": "User",
    "stock": "Stock",
    "sample-entities": "SampleEntity",
    "recensioni": "Review",
    "prodotti": "Product",
    "organizzatore-eventi": "EventManager",
    "ordini": "Order",
    "noleggi": "Rental",
    "metodi-pagamento": "PaymentMethod",
    "indirizzi-utente": "UserAddress",
    "filiale": "Branch",
    "dipendenti": "Employee",
    "dettagli-ordini": "OrderDetail",
    "dettagli-noleggi": "RentalDetail",
    "carrello": "Cart"
}

def get_entity_name_from_path(path):
    """
    Estrae il nome dell'entità dal percorso API. Se non trova una corrispondenza
    nella mappatura, restituisce il nome dell'entità così com'è, capitalizzato.
    """
    cleaned_path = path.replace('/api/', '')
    segment = re.sub(r'\{[^}]+\}', '', cleaned_path).split('/')[0]

    # Cerca il segmento nel mapping.
    if segment in ENTITY_MAPPING:
        return ENTITY_MAPPING[segment]

    # Se non c'è una mappatura, restituisce il nome dell'entità capitalizzato
    # senza tentare di fare la conversione da plurale a singolare.
    print(f"Attenzione: Nessuna mappatura trovata per '{segment}'. Verrà usato il nome così com'è, capitalizzato.")
    return segment.capitalize()

def create_operation_id(method, path):
    """
    Genera un operationId descrittivo basato sul metodo HTTP e sul nome dell'entità.
    """
    entity_name = get_entity_name_from_path(path)
    is_specific_resource = bool(re.search(r'\{[^}]+\}', path))

    if method == 'get':
        if is_specific_resource:
            return f"get{entity_name}ById"
        else:
            return f"getAll{entity_name}s"
    elif method == 'post':
        return f"create{entity_name}"
    elif method == 'put':
        return f"update{entity_name}"
    elif method == 'delete':
        return f"delete{entity_name}"

    return f"{method}{entity_name}"

def process_openapi_file(file_path):
    """
    Carica il file OpenAPI, genera gli operationId univoci e salva il file.
    """
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            openapi_spec = yaml.safe_load(f)
    except FileNotFoundError:
        print(f"Errore: Il file {file_path} non è stato trovato.", file=sys.stderr)
        sys.exit(1)
    except (yaml.YAMLError, ValueError) as e:
        print(f"Errore durante il parsing del file: {e}", file=sys.stderr)
        sys.exit(1)

    if 'paths' not in openapi_spec:
        print("Il file OpenAPI non contiene la sezione 'paths'.", file=sys.stderr)
        return

    print("--- Generazione OperationId ---")
    for path, methods in openapi_spec['paths'].items():
        for method, details in methods.items():
            current_operation_id = details.get('operationId', '')

            if not current_operation_id or re.search(r'_\d+$', current_operation_id):
                new_operation_id = create_operation_id(method, path)
                details['operationId'] = new_operation_id
                print(f"Assegnato 'operationId': {new_operation_id} per {method.upper()} {path}")
            else:
                print(f"OperationId '{current_operation_id}' già presente e valido per {method.upper()} {path}. Nessuna modifica.")

    try:
        with open(file_path, 'w', encoding='utf-8') as f:
            yaml.safe_dump(openapi_spec, f, sort_keys=False, default_flow_style=False, allow_unicode=True)
    except Exception as e:
        print(f"Errore durante il salvataggio del file: {e}", file=sys.stderr)
        sys.exit(1)

    print(f"\nFile OpenAPI modificato e salvato con successo: {file_path}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python generate_unique_operation_ids.py <percorso_file_openapi.json/yaml>")
        sys.exit(1)

    openapi_file = sys.argv[1]
    process_openapi_file(openapi_file)
