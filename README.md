<div align="center">

# 🧭 Reestruturação Arquitetural — Projeto Semestral FATEC

## Sistema de Gestão (Web + Mobile) sobre uma arquitetura em camadas

**Proposta de reestruturação de um sistema acadêmico PHP para uma arquitetura em camadas: Angular/Ionic → REST (JWT) → Java/Spring Boot → PostgreSQL, com uma trilha de dados em Python alimentando analytics.**

<br>

<img src="https://img.shields.io/badge/Projeto-Acadêmico-0F172A?style=for-the-badge" />
<img src="https://img.shields.io/badge/FATEC-Zona%20Sul-2563EB?style=for-the-badge" />
<img src="https://img.shields.io/badge/Curso-DSM-1D4ED8?style=for-the-badge" />
<img src="https://img.shields.io/badge/Status-Em%20definição-C79A2E?style=for-the-badge" />

<br><br>

### ⚙️ Stack Principal

<img src="https://img.shields.io/badge/Angular-Frontend-DD0031?style=for-the-badge&logo=angular&logoColor=white" />
<img src="https://img.shields.io/badge/Ionic-Mobile-3880FF?style=for-the-badge&logo=ionic&logoColor=white" />
<img src="https://img.shields.io/badge/Java-Backend-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
<img src="https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
<img src="https://img.shields.io/badge/Python-Analytics-3776AB?style=for-the-badge&logo=python&logoColor=white" />

<br><br>

### 🗄️ Banco, Deploy e Segurança

<img src="https://img.shields.io/badge/PostgreSQL-Relacional-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" />
<img src="https://img.shields.io/badge/Supabase-Produção-3FCF8E?style=for-the-badge&logo=supabase&logoColor=white" />
<img src="https://img.shields.io/badge/Docker-Containers-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
<img src="https://img.shields.io/badge/Auth-JWT%20%2B%20Spring%20Security-F59E0B?style=for-the-badge" />
<img src="https://img.shields.io/badge/Banco-Relacional-10B981?style=for-the-badge" />

<br><br>

## 📋 Gestão do Projeto

O desenvolvimento e acompanhamento das tarefas são realizados através do GitHub Projects.

👉 [Acessar o Project](https://github.com/users/Luck16Ash/projects/2/views/1)

### 🧩 Organização do Sistema

<img src="https://img.shields.io/badge/Arquitetura-Camadas-7C3AED?style=for-the-badge" />
<img src="https://img.shields.io/badge/Foco-Reorganização%20Arquitetural-020617?style=for-the-badge" />
<img src="https://img.shields.io/badge/Web-Angular-EC4899?style=for-the-badge" />
<img src="https://img.shields.io/badge/Mobile-Ionic%2FAndroid-64748B?style=for-the-badge" />
<img src="https://img.shields.io/badge/Dados-Python%20ETL-FACC15?style=for-the-badge" />

</div>

---

## 📌 Sobre o projeto

Este repositório documenta a **proposta de reestruturação arquitetural** de um sistema acadêmico do curso de **Desenvolvimento de Software Multiplataforma (DSM) da FATEC Zona Sul**, unindo três disciplinas em um único projeto:

- **Laboratório Web** — frontend Angular consumindo REST;
- **Mobile I** — app Ionic + Angular, compilável para Android;
- **Integração e Entrega Contínua** — Git, GitHub, CI/CD, Docker e deploy.

O núcleo comum às três frentes é **Java + Spring Boot + PostgreSQL**.

> ⚠️ Este documento descreve uma **proposta em andamento**, não o produto final. Regras de negócio, telas e escopo completo do sistema ainda serão fechados pelo grupo. A seção **Riscos Hipotéticos** (item 10) é uma análise técnica preventiva, não um relatório de bugs reais — o sistema ainda não está em operação.

---

## 🎯 Problema identificado

O sistema anterior nasceu em PHP no 2º semestre e, ao longo dos semestres seguintes, acumulou tecnologia solta e decisões tomadas sob prazo:

| Problema | Impacto na operação |
|---|---|
| Alto acoplamento entre camadas | Alterações em um ponto quebram outros sem aviso |
| Responsabilidades misturadas por tecnologia | Difícil saber onde cada regra de negócio "mora" |
| Sem autenticação estruturada | Nenhum controle formal de quem pode fazer o quê |
| Integração pouco clara entre partes | Fluxo de dados difícil de rastrear e depurar |
| Nenhuma camada de dados/analytics | Sistema não gera indicadores de uso próprios |

O diagnóstico do grupo: **o problema não é ter várias tecnologias — é não ter uma função clara para cada uma.**

---

## 🔄 Mudança de direção

A proposta não parte de uma reescrita completa nem de "trocar tecnologia por trocar". A virada é conceitual: em vez de continuar empilhando código sobre a base PHP, o sistema é reorganizado por **responsabilidade**, camada por camada, mantendo os mesmos conceitos que parte do grupo já domina (boa parte já usa Node.js/Express — a tabela de mapeamento na seção 5 mostra a equivalência).

```txt
antes → tecnologias soltas, sem hierarquia clara
depois → Web/Mobile → REST (JWT) → Spring Boot → PostgreSQL → Python (analytics)
```

---

## 🧭 Fluxo central do sistema (MVP)

```txt
LOGIN (JWT)
    ↓
LISTAGEM DE PRODUTOS
    ↓
CRIAÇÃO DE PEDIDO
    ↓
CONSULTA DE PEDIDOS
    ↓
DADOS OPERACIONAIS → PYTHON → ANALYTICS
```

Esse fluxo não representa o produto final — é a menor fatia que atravessa **todas** as camadas (login → listar produto → criar pedido, ponta a ponta) antes de empilhar o restante do escopo.

---

## 🧱 Tecnologias utilizadas

| Categoria | Tecnologias |
|---|---|
| Frontend Web | Angular |
| Mobile | Ionic + Angular (build Android) |
| Backend | Java + Spring Boot |
| Segurança | Spring Security + JWT stateless |
| Banco de dados | PostgreSQL (produção via Supabase) |
| Dados / Analytics | Python (ETL, tratamento, dashboards) |
| Containerização | Docker / docker-compose (backend, database, python) |
| Versionamento | Git e GitHub (Pull Requests obrigatórios) |
| CI/CD | Pipeline: build → testes → Docker → deploy |

### Mapeamento conceitual (para quem já usa Node.js/Express)

| Conhecido (PHP / Node.js) | Novo stack | Papel |
|---|---|---|
| Rotas Express | Controllers Spring Boot | Recebe a requisição HTTP |
| Middleware de auth | Filtro JWT (Spring Security) | Valida quem está chamando |
| Models (Mongoose/Sequelize) | Entidades JPA | Representa a tabela no banco |
| `.env` | `application.properties` / variáveis de ambiente | Configuração por ambiente |

---

## 🏗️ Arquitetura

O sistema segue uma arquitetura em camadas, com separação explícita entre apresentação, API, regras de negócio e dados:

```txt
Angular (Web) ─┐
                ├─► REST API (JWT) ─► Spring Boot ─► PostgreSQL
Ionic (Mobile) ─┘                         │
                                           └─► Python (ETL / Analytics / Dashboard)
```

- Web e Mobile **nunca** acessam o banco diretamente — toda regra de negócio passa pelo backend Java.
- Autenticação **stateless**: nenhuma sessão em memória no servidor, apenas validação de token por requisição.
- Python opera como consumidor secundário dos dados operacionais, não como sistema transacional.

Estrutura de referência no frontend Angular:

```txt
components/     → header, navbar, footer, product-card
pages/          → home, products, login
services/       → api.service.ts, auth.service.ts
guards/         → auth.guard.ts
```

---

## 🧩 Módulos previstos no MVP

### 🔐 Autenticação

- login com e-mail e senha (`POST /auth/login`);
- geração de JWT assinado (id + role) via Spring Security;
- token reenviado em `Authorization: Bearer <token>`;
- liberação/negação de rotas por role em filtro do Spring Security.

### 🛒 Catálogo e pedidos

- listagem de produtos (`GET /produtos`);
- cadastro de produto restrito a ADMIN (`POST /produtos`);
- criação de pedido pelo cliente (`POST /pedidos`);
- consulta de pedidos, geral (ADMIN) ou próprios (CLIENTE).

### 📱 Protótipo mobile

- Ionic + Angular, compilável para Android;
- navegação real entre telas (login → produtos → produto → carrinho → confirmação → pedidos);
- cada ação dispara uma chamada real à mesma API usada pelo Web — não há backend separado para mobile.

### 📊 Dados & Analytics

Pipeline: `PostgreSQL → Python → ETL → Tratamento → Analytics → Dashboard`. Indicadores previstos: produto mais vendido, faturamento mensal, ticket médio, estoque baixo.

---

## 🔐 Permissões

```txt
ADMIN
CLIENTE
```

| Perfil | Acesso principal |
|---|---|
| ADMIN | Cadastrar/editar produtos, ver todos os pedidos |
| CLIENTE | Ver produtos, criar e ver os próprios pedidos |

> Onde **não** guardar o token: `localStorage` no Angular é vulnerável a XSS. Preferir token em memória (serviço Angular) + refresh via cookie `httpOnly`. No Ionic, usar plugin de secure storage nativo — não o storage do WebView.

---

## 🛣️ Rotas mínimas do MVP

| Rota | Método | Acesso | Descrição |
|---|---|---|---|
| `/auth/login` | POST | Público | Autenticação e emissão de JWT |
| `/produtos` | GET | Autenticado | Lista de produtos |
| `/produtos` | POST | ADMIN | Cadastro de produto |
| `/pedidos` | GET | Autenticado | Lista de pedidos (próprios ou todos, por role) |
| `/pedidos` | POST | CLIENTE | Criação de pedido |
| `/pedidos/:id` | GET | Autenticado | Detalhe de um pedido |

---

## 🗄️ Banco de dados

O projeto utiliza **PostgreSQL**, modelo relacional:

```txt
clientes (id PK, nome)
    └── 1:N → pedidos (id PK, cliente_id FK)
                  └── 1:N → itens_pedido (id PK, pedido_id FK, produto_id FK)
                                  └── N:1 → produtos (id PK, categoria_id FK)
```

Entidades mínimas do MVP: `Usuario(id, nome, email, senha_hash, role)`, `Cliente(id, nome, contato)`, `Produto(id, nome, preco, estoque)`, `Pedido(id, cliente_id, status, criado_em)`, `ItemPedido(id, pedido_id, produto_id, qtd)`.

Em produção, o PostgreSQL é hospedado via **Supabase** — plataforma construída sobre PostgreSQL, não apenas um banco gerenciado.

---

## 🐳 Containerização e CI/CD

```txt
docker-compose.yml
├─ backend     (Spring Boot)
├─ database    (PostgreSQL)
└─ python      (ETL / analytics)
```

Pipeline de CI/CD: `Codar → Git → Pull Request → CI → Testes → Build → Docker → Deploy → Produção`.

> Docker padroniza o ambiente entre as máquinas do grupo — não elimina a necessidade de tê-lo instalado localmente.

---

## ⚙️ Deploy

```txt
Internet
  ├── Frontend Angular (hospedagem própria)
  └── Mobile (build Android)
        │ HTTPS
        ▼
  Java / Spring Boot (backend)
        ├── PostgreSQL (Supabase)
        └── Python (ETL / Analytics / Dashboard)
```

---

## ▶️ Como executar localmente *(referência — repositório e comandos definitivos a confirmar pelo grupo)*

Clone o repositório:

```bash
git clone https://github.com/<organizacao-ou-usuario>/<nome-do-repo>.git
```

Acesse a pasta:

```bash
cd <nome-do-repo>
```

Suba os serviços com Docker:

```bash
docker-compose up -d
```

Backend (Spring Boot) e frontend (Angular) sobem como containers separados, definidos no `docker-compose.yml`.

---

## 👥 Divisão do grupo

| Frente | Responsabilidades |
|---|---|
| **Backend** | Java, Spring Boot, Spring Security/JWT, PostgreSQL, regras de negócio |
| **Mobile/Frontend** | Angular, Ionic, Guards/Interceptors, componentes, integração com API |
| **Dados** | Python, SQL, ETL, Analytics, Dashboard |
| **Todos** | Git/GitHub, Pull Requests, CI/CD, documentação |

---

## 📅 Status do projeto

| Definido ✅ | Em definição 🟡 | Possíveis evoluções 🔵 |
|---|---|---|
| Arquitetura em camadas | Funcionalidades finais do sistema | Analytics e dashboards avançados |
| Stack completa (Angular, Ionic, Java/Spring, PostgreSQL, Python, Docker) | Regras de negócio específicas | Automações e notificações |
| Responsabilidade de cada camada | Escopo definitivo de telas e relatórios | Novas funcionalidades sob demanda |
| MVP mínimo (login, produtos, pedidos) | | |

---

## ⚠️ Riscos hipotéticos e mitigações propostas

> Não existe ainda uma lista real de problemas — o projeto está em fase de proposta, não de operação. A tabela abaixo é uma análise técnica preventiva (o que tende a dar errado nesse tipo de arquitetura, com esse nível de maturidade de equipe), a ser substituída por incidentes reais conforme o grupo avança.

| Área | Risco hipotético | Gravidade | Mitigação proposta |
|---|---|---|---|
| Segurança | JWT sem mecanismo de revogação | Alto | TTL curto + refresh token com rotação |
| Segurança | Hash de senha fraco ou ausente | Crítico | BCrypt explícito (padrão Spring Security) |
| Segurança | Segredos versionados no Git (`docker-compose.yml`) | Crítico | Variáveis de ambiente + `.gitignore` |
| Banco de dados | Condição de corrida no decremento de `estoque` | Alto | Lock otimista (`@Version`) ou `SELECT ... FOR UPDATE` |
| Banco de dados | Criação de pedido + itens sem transação atômica | Alto | `@Transactional` no service de criação de pedido |
| Banco de dados | Ausência de ferramenta de migração de schema | Médio | Adotar Flyway/Liquibase desde o MVP |
| Arquitetura | Python consultando o banco transacional sem isolamento | Médio | Job agendado fora de pico ou réplica de leitura |
| DevOps | Etapa "Testes" no CI sem cobertura mínima obrigatória | Médio | Definir limiar de cobertura e falhar build abaixo dele |
| Acadêmico | Escopo de "evoluções" consumir tempo do MVP obrigatório | Alto | Congelar o MVP antes de iniciar itens de evolução |

---

## 📈 Resultado esperado

Um sistema em produção, acessível pela internet, com backend único servindo Web e Mobile, autenticação por papel funcionando desde o MVP, dados operacionais reais alimentando uma camada de analytics, pipeline de CI/CD funcional (build, testes, Docker, deploy) e um portfólio técnico mais próximo de uma arquitetura profissional — cumprindo simultaneamente os requisitos das três disciplinas envolvidas.

---

## 📌 Observações importantes

- Este é um documento de **proposta arquitetural**, não a especificação final do produto.
- O escopo completo de telas, relatórios e regras de negócio ainda será definido pelo grupo.
- A lista de riscos hipotéticos deve ser revisada e substituída por casos reais assim que o projeto entrar em desenvolvimento ativo.
- Nenhuma credencial real deve ser versionada — usar `.env` local e variáveis de ambiente no provedor de deploy.

---

## 🎓 Contexto acadêmico

Projeto semestral do curso de **Desenvolvimento de Software Multiplataforma (DSM)**, FATEC Zona Sul, integrando as disciplinas de Laboratório Web, Mobile I e Integração e Entrega Contínua em um único sistema.
