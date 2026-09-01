<div align="center">

# 🎀 PM Bem-Casados

**Vitrine digital e gestão de encomendas, produção e estoque para confeitaria artesanal**

Projeto Integrador · Desenvolvimento de Software Multiplataforma · 4º Semestre

![Status](https://img.shields.io/badge/status-planejamento%2Fdesenvolvimento-yellow)
![Entrega](https://img.shields.io/badge/entrega-13%2F11%2F2026-blue)
![Licença](https://img.shields.io/badge/licença-acadêmico-lightgrey)

</div>

---

## 📋 Índice

- [Sobre o projeto](#-sobre-o-projeto)
- [O problema](#-o-problema)
- [A solução](#-a-solução)
- [Escopo do MVP](#-escopo-do-mvp)
- [Arquitetura](#-arquitetura)
- [Stack tecnológica](#-stack-tecnológica)
- [Modelo de dados e privacidade](#-modelo-de-dados-e-privacidade)
- [Estrutura do repositório](#-estrutura-do-repositório)
- [Como rodar o projeto](#-como-rodar-o-projeto)
- [Equipe](#-equipe)
- [Roadmap](#-roadmap)
- [Status atual](#-status-atual)
- [Licença](#-licença)

---

## 🧁 Sobre o projeto

**PM Bem-Casados** é uma profissional autônoma que produz bem-casados, doces e itens personalizados para casamentos, aniversários, chá de bebê, chá revelação, formaturas e outras celebrações, em produção artesanal por encomenda, sem equipe ou estrutura industrial.

Hoje, toda a operação — catálogo, atendimento, controle de insumos — acontece de forma manual, via WhatsApp, Instagram, Facebook e um caderno de anotações.

Este projeto **não é uma loja virtual**. É uma ferramenta de organização que reduz trabalho repetitivo e dá visibilidade sobre o negócio, mantendo o fechamento do pedido onde ele já funciona bem: uma conversa real pelo WhatsApp.

## 🎯 O problema

| Problema | Descrição |
|---|---|
| **Vitrine desorganizada** | Catálogo existe apenas em fotos soltas no celular e redes sociais — difícil de navegar, comparar ou compartilhar. |
| **Atendimento repetitivo** | Cada pedido começa do zero: produto, quantidade, data e local são perguntados manualmente a cada conversa. |
| **Estoque de cabeça** | Sem controle de ingredientes e embalagens, o risco de faltar insumo em uma encomenda fechada é decidido na memória. |

## 💡 A solução

Um sistema com **duas portas de acesso**:

**Ambiente do cliente** (público, sem login)
- Vitrine e catálogo de produtos
- Jornada guiada "monte sua encomenda" (não é um formulário longo)
- Definição de evento e data
- Envio estruturado da solicitação para o WhatsApp

**Ambiente da administradora** (autenticado)
- Cadastro de produtos e ficha técnica
- Gestão de encomendas e produção (fluxo de status)
- Controle de estoque e insumos, com alerta de estoque mínimo
- Indicadores do negócio

### Jornada do cliente

```
Vitrine → Monte o pedido → Sobre o evento → Local e dados → Revisão → Envio ao WhatsApp
```
Faixas de quantidade pré-definidas (50 · 100 · 250 unidades) com estimativa inicial de valor — não é o orçamento final. Acima de 250 unidades, o sistema direciona para consulta direta pelo WhatsApp.

### Fluxo operacional

```
Solicitação → Análise/Negociação → Confirmação → Agendado → Em produção → Pronto → Entregue
```

## ✅ Escopo do MVP

| Entra no MVP | Fora do escopo |
|---|---|
| Vitrine digital com catálogo | Pagamento online, gateway ou checkout |
| Jornada guiada de encomenda | Marketplace ou chat próprio |
| Envio estruturado para o WhatsApp | Rastreamento de entrega em tempo real |
| Estoque de insumos com alerta mínimo | Sistema fiscal / contabilidade / ERP |
| Ficha técnica de produtos | IA / Machine Learning avançado |
| Indicadores básicos de negócio | Integrações pagas e logística complexa |

> Escopo reduzido por decisão, não por falta de ambição: o objetivo é garantir que o que entra funcione bem dentro do prazo de um semestre.

## 🏗 Arquitetura

Arquitetura em camadas, padrão MVC no backend:

```
Cliente (Angular / Ionic)
        ↓ HTTP
   Controller  (recebe requisições)
        ↓
    Service    (regras de negócio)
        ↓
   Repository  (acesso a dados)
        ↓
   PostgreSQL
```

**Regras de negócio centrais:** produto inativo não aparece na vitrine · quantidade influencia a estimativa · pedido segue fluxo de status definido · estoque mínimo dispara alerta.

**Segurança:** Spring Security + JWT — apenas a administradora autenticada acessa o painel e as rotas administrativas; o restante da API é público, sem exigir cadastro do cliente.

Em paralelo, um fluxo de analytics consome o mesmo banco:

```
PostgreSQL → ETL (extração/limpeza) → Python (processamento) → Indicadores → Dashboard
```
Inclui estimativa simples de demanda a partir de histórico — **sem Machine Learning complexo**.

## 🛠 Stack tecnológica

| Camada | Tecnologia |
|---|---|
| Backend | Java · Spring Boot · Spring Security (JWT) |
| Frontend Web | Angular |
| Mobile | Ionic |
| Banco de dados | PostgreSQL |
| Dados / Analytics | Python (ETL e indicadores) |
| Infraestrutura | Docker |
| Versionamento | Git / GitHub (Issues, branches, Pull Requests) |

## 🔒 Modelo de dados e privacidade

Minimização de dados por padrão — coleta-se apenas o necessário para organizar a encomenda:

**Coletado:** nome, WhatsApp, e-mail (quando necessário), data e local do evento, dados da encomenda.
**Não coletado:** CPF, RG, documentos, dados bancários, foto de documento, data de nascimento completa.

> ⚠️ **Em aberto:** prazo de retenção de dados após a entrega e o texto final do aviso de privacidade ainda dependem de definição com a cliente. Este README será atualizado assim que estiverem fechados — não tratar como implementado até então.

## 📁 Estrutura do repositório

```
pm-bem-casados/
├── backend/     # API Java + Spring Boot
├── frontend/    # Aplicação Angular
├── mobile/      # Aplicação Ionic
├── data/        # ETL, scripts Python, analytics
├── docs/        # Documentação do projeto (PMC, requisitos, modelagem)
├── docker-compose.yml
└── README.md
```

## 🚀 Como rodar o projeto

> Esta seção será preenchida com instruções reais (pré-requisitos, variáveis de ambiente, comandos) assim que o `docker-compose.yml` e os serviços de backend/frontend/mobile estiverem implementados. Até lá, evitamos publicar comandos que ainda não funcionam.

## 👥 Equipe

Projeto dividido em três frentes que constroem em paralelo, com entregáveis conectados e revisão por Pull Request:

| Frente | Integrantes | Responsabilidade |
|---|---|---|
| Backend / Java | 2 | Spring Boot, API, banco de dados, segurança, regras de negócio |
| Frontend / Mobile | 2 | Angular, Ionic, UX/UI, integração com a API |
| Dados | 2 | SQL, Python, ETL, Analytics e dashboard |

## 🗺 Roadmap

| Período | Fase | Entregas |
|---|---|---|
| Fim de agosto / setembro | Descoberta | Entender o negócio (PMC), dores, requisitos e escopo |
| Setembro | Fundação | GitHub, banco de dados, arquitetura, setup Spring Boot / Angular / Ionic / Docker |
| Fim de setembro / outubro | MVP | Vitrine, produtos, encomendas, envio ao WhatsApp, painel, estoque |
| Outubro | Integração | Autenticação, segurança, validações, testes, documentação da API |
| Fim de outubro / início de novembro | Dados | ETL, Python, indicadores, dashboard, previsão simples |
| 09–13/11 | Entrega | Testes finais, correções, deploy, documentação, validação |

## 📌 Status atual

- [x] PMC (proposta de modelo de negócio) — em elaboração
- [ ] Validação das dores levantadas com a cliente real
- [ ] Requisitos funcionais e não funcionais fechados
- [ ] Modelagem do banco de dados
- [ ] Arquitetura detalhada
- [ ] Estrutura do repositório
- [ ] Prototipação de telas
- [ ] Início do desenvolvimento

*Este README reflete o planejamento do projeto. Seções marcadas como "em aberto" serão atualizadas conforme as decisões forem fechadas com a cliente e a equipe.*

## 📄 Licença

Projeto acadêmico desenvolvido para a disciplina de Projeto Integrador — Desenvolvimento de Software Multiplataforma. Uso restrito a fins educacionais.

---

<div align="center">
Do caderno de anotações e das mensagens soltas no WhatsApp para um sistema que organiza — sem tirar da PM Bem-Casados o cuidado artesanal em cada encomenda.
</div>
