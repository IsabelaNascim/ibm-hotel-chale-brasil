# ibm-hotel-chale-brasil

API Endpoints PRODUÇÃO:
Aqui estão os endpoints disponíveis na API e suas descrições detalhadas.

Parâmetros:
{
    "idReserva": ,
    "hospede": "",
    "quantHospedes": ,
    "checkIn": "yyyy/MM/dd",
    "checkOut": "yyyy/MM/dd",
    "status"
}


id (Path Parameter) - O ID único da reserva.
> POST api/reservas
PATH: /https://ibm-hotel-chale-brasil-production.up.railway.app/reservas
Descrição: Cria uma nova reserva de um hóspede no sistema.

Corpo da Requisição (JSON):
{
    "hospede": "Marcela Galdino Paulo",
    "checkIn": "2023/12/01",
    "checkOut": "2023/12/03",
    "quantHospedes": 4
}

> GET /api/reservas/all
PATH: ibm-hotel-chale-brasil-production.up.railway.app/reservas/all
Descrição: Retorna uma lista de todos as reservas registrados no sistema.

Corpo da Requisição (JSON): vazio

> GET /api/reservas/{id}
PATH: ibm-hotel-chale-brasil-production.up.railway.app/reservas/{id}
Descrição: Retorna informações detalhadas sobre uma reserva específica.

Corpo da Requisição (JSON): vazio

> PUT /api/reservas/{id}
PATH: https://ibm-hotel-chale-brasil-production.up.railway.app/reservas/{id}
Descrição: Atualiza as informações de uma reserva existente e atualiza o status para "PENDENTE".

Corpo da Requisição (JSON):
{
    "hospede": "Marcela Galdino Paulo Nascimento",
    "checkIn": "2023/12/02",
    "checkOut": "2023/12/03",
    "quantHospedes": 5
}

> PUT /api/reservas/cancelar/{id}
PATH: https://ibm-hotel-chale-brasil-production.up.railway.app/reservas/cancelar/{id}
Descrição: Atualiza o status da reserva para "CANCELADA".

Corpo da Requisição (JSON): vazio



