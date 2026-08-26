# PROJETO-SEMESTRAL-4

# Reestruturação Arquitetural — Projeto Semestral FATEC (DSM)

> **Status:** em definição. Este documento descreve a *proposta de arquitetura* apresentada ao grupo, não o produto final. Regras de negócio, telas e escopo completo ainda serão fechados coletivamente. As seções de **riscos** abaixo são **hipotéticas** — não existe ainda um levantamento real de bugs/incidentes, pois o sistema está em fase de reestruturação, não de operação.

---

## 1. Objetivo Principal

Substituir um sistema acadêmico originalmente escrito em PHP (2º semestre), que acumulou tecnologia solta e responsabilidades misturadas ao longo dos semestres, por uma arquitetura em camadas com responsabilidade única por tecnologia — reaproveitando o mesmo backend para Web e Mobile e gerando dados operacionais reais que alimentam uma camada de analytics.

O projeto une três disciplinas em um único sistema, em vez de três entregas isoladas:

| Disciplina | Frente |
|---|---|
| Laboratório Web | Frontend Angular consumindo REST |
| Mobile I | App Ionic + Angular (compilável para Android) |
| Integração e Entrega Contínua | Git, GitHub, CI/CD, Docker, Deploy |

Núcleo comum às três frentes: **Java + Spring Boot + PostgreSQL**.

## 2. Problema de Origem

O sistema anterior (PHP/EJS) não tinha função clara por tecnologia: alto acoplamento, código acumulado, responsabilidades misturadas, sem autenticação estruturada e integração pouco clara entre as partes. O diagnóstico do grupo é que o problema **não é ter várias tecnologias, é não ter uma função definida para cada uma**.

## 3. Arquitetura Proposta

```
Angular (Web) ─┐
                ├─► REST API (JWT) ─► Spring Boot ─► PostgreSQL
Ionic (Mobile) ─┘                         │
                                           └─► Python (ETL/Analytics/Dashboard)
```

- Cliente Web e Mobile **nunca** acessam o banco diretamente — toda regra de negócio passa pelo Spring Boot.
- Autenticação stateless via **JWT** (Spring Security), sem sessão em memória no backend.
- Python opera como consumidor secundário dos dados gerados pela operação (não é o sistema transacional).

### Stack

| Camada | Tecnologia |
|---|---|
| Frontend Web | Angular |
| Mobile | Ionic + Angular (build Android) |
| Backend | Java + Spring Boot + Spring Security (JWT) |
| Banco de dados | PostgreSQL (produção via Supabase) |
| Dados / Analytics | Python (ETL, tratamento, dashboards) |
| Containerização | Docker / docker-compose (backend, database, python) |
| CI/CD | Git, GitHub, Pull Requests, pipeline de CI |
| Deploy | Frontend e mobile hospedados separadamente; backend em serviço próprio; banco via Supabase |

### Mapeamento conceitual (para quem já usa Node.js/Express)

A proposta reaproveita conceitos já conhecidos do grupo (Node/Express e do PHP anterior), apenas realocando onde cada peça mora — não é uma reintrodução do zero.

## 4. MVP Mínimo

Fatia mínima que atravessa todas as camadas (Angular/Ionic → REST → Spring Boot → PostgreSQL) com login funcional, antes de qualquer escopo adicional.

**Entidades:** `Usuario(id, nome, email, senha_hash, role)`, `Cliente(id, nome, contato)`, `Produto(id, nome, preco, estoque)`, `Pedido(id, cliente_id, status, criado_em)`, `ItemPedido(id, pedido_id, produto_id, qtd)`

**Endpoints:**
```
POST   /auth/login
GET    /produtos
POST   /produtos        (ADMIN)
GET    /pedidos
POST   /pedidos         (CLIENTE)
GET    /pedidos/:id
```

## 5. Autenticação & Autorização

Fluxo: credenciais → validação no PostgreSQL via Spring Security → JWT assinado (id + role) → cliente reenvia via `Authorization: Bearer <token>` → filtro do Spring Security libera/nega por role.

| Papel | Permissões |
|---|---|
| ADMIN | Cadastrar/editar produtos, ver todos os pedidos |
| CLIENTE | Ver produtos, criar e ver os próprios pedidos |

Decisão já registrada na proposta: **não** usar `localStorage` no Angular (vulnerável a XSS) — token em memória (serviço Angular) + refresh via cookie `httpOnly`; no Ionic, usar secure storage nativo, não o storage do WebView.

## 6. Banco de Dados

Modelo relacional: `clientes 1:N pedidos`, `pedidos 1:N itens_pedido`, `itens_pedido N:1 produtos`, `produtos N:1 categorias`. PostgreSQL em produção via Supabase.

## 7. Dados & Analytics

Pipeline: `PostgreSQL → Python → ETL → Tratamento → Analytics → Dashboard`. Indicadores previstos: produto mais vendido, faturamento mensal, ticket médio, estoque baixo.

## 8. Deploy

```
Internet
  ├── Frontend Angular (hospedagem própria)
  └── Mobile (build Android)
        │ HTTPS
        ▼
  Java / Spring Boot (backend)
        ├── PostgreSQL (Supabase)
        └── Python (ETL / Analytics / Dashboard)
```

## 9. Divisão do Grupo

| Frente | Responsabilidades |
|---|---|
| Backend | Java, Spring Boot, Spring Security/JWT, PostgreSQL, regras de negócio |
| Mobile/Frontend | Angular, Ionic, Guards/Interceptors, componentes, integração com API |
| Dados | Python, SQL, ETL, Analytics, Dashboard |
| Todos | Git/GitHub, Pull Requests, CI/CD, documentação |

## 10. Status

| Definido | Em definição | Possíveis evoluções |
|---|---|---|
| Arquitetura em camadas | Funcionalidades finais do sistema | Analytics e dashboards avançados |
| Stack completa | Regras de negócio específicas | Automações e notificações |
| Responsabilidade de cada camada | Escopo definitivo de telas e relatórios | Novas funcionalidades sob demanda |
| MVP mínimo (login, produtos, pedidos) | | |

## 11. Resultado Esperado

Um sistema em produção (acessível pela internet) com backend único servindo Web e Mobile, autenticação por papel funcionando desde o MVP, dados operacionais reais alimentando uma camada de analytics, pipeline de CI/CD funcional (build, testes, Docker, deploy) e um portfólio técnico mais próximo de uma arquitetura profissional do que o projeto PHP original — cumprindo simultaneamente os requisitos de três disciplinas (Laboratório Web, Mobile I, Integração e Entrega Contínua).

---

## 12. Riscos Hipotéticos e Mitigações Propostas

**Aviso:** não existe ainda uma lista real de problemas — o projeto está em fase de proposta, não de operação. A lista abaixo é uma análise técnica preventiva (o que tende a dar errado nesse tipo de arquitetura, com esse nível de maturidade de equipe), não um relatório de incidentes reais. Ela deve ser revisada e substituída à medida que o grupo fechar as regras de negócio.

### Segurança

| Risco hipotético | Gravidade | Mitigação proposta |
|---|---|---|
| Token JWT sem mecanismo de revogação — comprometido antes de expirar, continua válido | Alto | TTL curto (ex.: 15 min) + refresh token com rotação; lista de revogação (blacklist/whitelist) se necessário |
| Hash de senha fraco ou ausente (ex.: MD5/SHA sem salt) | Crítico | Definir explicitamente BCrypt (padrão do Spring Security) e documentar no README técnico do backend |
| CORS mal configurado (bloqueando tudo em dev ou liberando `*` em produção) | Médio | Definir política de CORS explícita por ambiente antes do primeiro deploy |
| Mass assignment / over-posting em endpoints `POST/PUT` sem DTOs de entrada | Alto | Usar DTOs de request separados das entidades JPA; nunca expor entidade diretamente |
| Segredos (senha do banco, chave de assinatura do JWT) hardcoded no `docker-compose.yml` ou versionados no Git | Crítico | Variáveis de ambiente + `.env` no `.gitignore` + secrets do provedor de deploy |

### Banco de Dados

| Risco hipotético | Gravidade | Mitigação proposta |
|---|---|---|
| Condição de corrida ao decrementar `estoque` em pedidos simultâneos (lost update) | Alto | Lock otimista (`@Version`) ou transação com `SELECT ... FOR UPDATE` na baixa de estoque |
| Criação de `Pedido` + `ItemPedido` sem limite de transação — falha parcial deixa pedido órfão | Alto | `@Transactional` no service que cria pedido, garantindo atomicidade |
| Ausência de estratégia de migração de schema (Flyway/Liquibase) entre máquinas do grupo | Médio | Adotar Flyway desde o MVP para versionar o schema junto com o código |
| Consultas sem paginação em `GET /produtos` e `GET /pedidos` conforme a base cresce | Médio | Paginação (`Pageable`) desde o MVP, mesmo com poucos dados hoje |
| Cenário de banco vazio não tratado (ex.: dashboard Python quebrando sem dados) | Baixo | Testar explicitamente os fluxos de analytics com base vazia/parcial |

### Arquitetura / Escalabilidade

| Risco hipotético | Gravidade | Mitigação proposta |
|---|---|---|
| Python (ETL/Analytics) consultando o mesmo PostgreSQL transacional sem isolamento | Médio | Job agendado fora de horário de pico, ou réplica de leitura se o volume justificar |
| Ausência de versionamento de API (`/produtos` sem `/v1/`) dificultando evolução futura | Baixo | Prefixar rotas com versão desde o início custa pouco e evita breaking changes depois |
| Nenhum rate limiting/API gateway — endpoint público exposto sem limite de chamadas | Médio | Rate limiting básico no Spring (bucket4j ou filtro simples) antes do deploy público |

### DevOps / Deploy

| Risco hipotético | Gravidade | Mitigação proposta |
|---|---|---|
| Pipeline de CI com etapa "Testes" nominal, mas sem cobertura mínima obrigatória | Médio | Definir um limiar mínimo de cobertura e falhar o build abaixo dele |
| Limites do tier gratuito do Supabase (conexões simultâneas, cold start) não avaliados antes do deploy | Médio | Validar limites do plano gratuito com a carga esperada do MVP antes da apresentação |
| `docker-compose.yml` funcionando localmente mas divergente do ambiente de deploy real | Alto | Usar a mesma imagem Docker do backend em dev e produção, não builds distintos |

### Processo / Acadêmico

| Risco hipotético | Gravidade | Mitigação proposta |
|---|---|---|
| Escopo de "possíveis evoluções" crescer e consumir tempo do MVP obrigatório | Alto | Congelar o MVP antes de iniciar qualquer item da coluna "possíveis evoluções" |
| Múltiplos integrantes editando o mesmo módulo do monólito Spring Boot em paralelo | Médio | Pacotes por domínio (auth, produtos, pedidos) bem isolados + PR obrigatório por feature |
| Regras de negócio "em definição" ainda não fechadas na data da banca | Alto | Definir uma data-limite interna para fechar regras de negócio, anterior ao prazo oficial |

---

## 13. Licença e Autores

Projeto acadêmico — Tecnologia em Desenvolvimento de Software Multiplataforma (DSM), FATEC Zona Sul.
