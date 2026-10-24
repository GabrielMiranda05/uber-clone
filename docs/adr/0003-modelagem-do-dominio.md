# ADR 0003: Modelagem do domínio

- **Status:** aceito
- **Data:** 2026-10-24

## Contexto

O domínio (usuários, motoristas, veículos, corridas, avaliações e pagamentos)
precisa de um modelo de dados estável antes das migrations e das entidades.
O detalhamento está em `docs/domain-model.md`.

## Decisão

- `users` com papel único + `driver_profiles` 1:1, em vez de tabelas separadas.
- Identificadores **UUID v7**, gerados pela aplicação.
- Dinheiro em **centavos inteiros** (`BIGINT`), moeda fixa BRL.
- Coordenadas em `DOUBLE PRECISION`; PostGIS fica para a Fase 7, se necessário.
- Máquina de estados da corrida validada no código; recusa não é estado.

## Consequências

- (+) Um único fluxo de autenticação; IDs não enumeráveis; sem erro de arredondamento.
- (-) Quem quiser ser passageiro e motorista precisa de duas contas.
- (-) Um veículo por motorista.
- (-) Sem consultas geoespaciais indexadas até a Fase 7.
