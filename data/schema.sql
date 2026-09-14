-- ============================================================
-- TIPOS (ENUMS)
-- São listas fixas de opções válidas, pra evitar que alguém digite
-- "cancelado" numa tabela e "Cancelado" ou "CANCELED" em outra.
-- ============================================================

-- Todas as etapas por onde um pedido pode passar, do pedido até a entrega
CREATE TYPE "status_pedido" AS ENUM (
  'SOLICITACAO',
  'ANALISE_NEGOCIACAO',
  'CONFIRMACAO',
  'AGENDADO',
  'PRONTO',
  'ENTREGUE',
  'CANCELADO'
);

-- Toda vez que o estoque de um insumo muda, é um destes três tipos de movimento
CREATE TYPE "tipo_movimentacao_estoque" AS ENUM (
  'ENTRADA',
  'SAIDA',
  'AJUSTE'
);

-- De onde veio essa mudança de estoque: comprou de fornecedor, usou pra produzir
-- um pedido, ou foi um ajuste manual (ex: contagem física deu diferente)
CREATE TYPE "origem_movimentacao_estoque" AS ENUM (
  'COMPRA_FORNECEDOR',
  'PRODUCAO_PEDIDO',
  'AJUSTE_MANUAL'
);

-- Região da cidade onde o fornecedor fica, usado pra filtrar/organizar
CREATE TYPE "regiao_fornecedor" AS ENUM (
  'ZONA_SUL',
  'ZONA_LESTE',
  'ZONA_OESTE',
  'ZONA_NORTE'
);

-- ============================================================
-- USUÁRIOS DO SISTEMA (administradoras da confeitaria)
-- ============================================================

CREATE TABLE "usuario_admin" (
  "id" uuid PRIMARY KEY,
  "nome" varchar NOT NULL,
  "email" varchar UNIQUE NOT NULL,
  "senha_hash" varchar NOT NULL,
  "criado_em" timestamp NOT NULL
);

-- ============================================================
-- CATÁLOGO DE PRODUTOS
-- ============================================================

-- Cores/tons disponíveis pra personalizar um produto (ex: dourado, rosé, branco)
CREATE TABLE "tom_cor" (
  "id" uuid PRIMARY KEY,
  "nome" varchar UNIQUE NOT NULL,
  "ativo" boolean NOT NULL,
  "criado_em" timestamp NOT NULL
);

-- Cada item que a confeitaria vende (bem-casado, docinho, etc.)
CREATE TABLE "produto" (
  "id" uuid PRIMARY KEY,
  "nome" varchar NOT NULL,
  "categoria" varchar,
  "personalizado" boolean NOT NULL,
  "preco_unitario" decimal NOT NULL,
  "sabor" varchar,
  "papel_embalagem" varchar,
  "tom_cor_id" uuid,
  "ativo" boolean NOT NULL,
  "foto_url" varchar,
  "criado_em" timestamp NOT NULL
);

-- Matéria-prima usada na produção (açúcar, embalagem, fita, etc.)
CREATE TABLE "insumo" (
  "id" uuid PRIMARY KEY,
  "nome" varchar NOT NULL,
  "unidade_medida" varchar NOT NULL,
  "quantidade_estoque" decimal NOT NULL,
  "quantidade_minima" decimal NOT NULL
);

-- A "receita" de cada produto: quanto de cada insumo é gasto pra fazer 1 unidade
CREATE TABLE "ficha_tecnica" (
  "produto_id" uuid NOT NULL,
  "insumo_id" uuid NOT NULL,
  "quantidade_necessaria" decimal NOT NULL,
  PRIMARY KEY ("produto_id", "insumo_id")
);

-- ============================================================
-- FORNECEDORES
-- ============================================================

-- De quem a confeitaria compra os insumos
CREATE TABLE "fornecedor" (
  "id" uuid PRIMARY KEY,
  "nome" varchar NOT NULL,
  "bairro" varchar,
  "regiao" regiao_fornecedor,
  "contato" varchar
);

-- Qual fornecedor vende qual insumo, e por quanto foi a última compra
CREATE TABLE "fornecedor_insumo" (
  "id" uuid PRIMARY KEY,
  "fornecedor_id" uuid NOT NULL,
  "insumo_id" uuid NOT NULL,
  "ultimo_preco" decimal,
  "ultima_compra_em" date
);

-- ============================================================
-- CLIENTES E LOGIN (sem senha — login por link no e-mail/WhatsApp)
-- ============================================================

CREATE TABLE "cliente" (
  "id" uuid PRIMARY KEY,
  "nome" varchar NOT NULL,
  "whatsapp" varchar UNIQUE NOT NULL,
  "email" varchar UNIQUE,
  "email_verificado_em" timestamp,
  "criado_em" timestamp NOT NULL,
  "consentimento_lgpd_em" timestamp NOT NULL
);

-- Os links de login (magic link) enviados por e-mail pro cliente, com validade curta
CREATE TABLE "cliente_login_token" (
  "id" uuid PRIMARY KEY,
  "cliente_id" uuid NOT NULL,
  "token" uuid UNIQUE NOT NULL,
  "expira_em" timestamp NOT NULL,
  "usado_em" timestamp,
  "criado_em" timestamp NOT NULL
);

-- ============================================================
-- PEDIDOS
-- ============================================================

-- O pedido em si: dados do evento, endereço de entrega e status atual
CREATE TABLE "pedido" (
  "id" uuid PRIMARY KEY,
  "cliente_id" uuid NOT NULL,
  "tipo_evento" varchar,
  "data_evento_solicitada" date,
  "data_evento_confirmada" date,
  "local_cep" varchar,
  "local_cidade" varchar,
  "local_bairro" varchar,
  "local_logradouro" varchar,
  "local_numero" varchar,
  "local_complemento" varchar,
  "status" status_pedido NOT NULL,
  "valor_estimado_inicial" decimal,
  "valor_confirmado" decimal,
  "consentimento_lgpd" boolean NOT NULL,
  "criado_em" timestamp NOT NULL,
  "atualizado_em" timestamp NOT NULL
);

-- Registro de cada mudança de status do pedido (auditoria: quem mudou, quando, por quê)
CREATE TABLE "pedido_status_historico" (
  "id" uuid PRIMARY KEY,
  "pedido_id" uuid NOT NULL,
  "status" status_pedido NOT NULL,
  "alterado_por" uuid,
  "motivo" text,
  "alterado_em" timestamp NOT NULL
);

-- Cada produto dentro de um pedido (um pedido pode ter vários itens)
CREATE TABLE "item_pedido" (
  "id" uuid PRIMARY KEY,
  "pedido_id" uuid NOT NULL,
  "produto_id" uuid NOT NULL,
  "quantidade" int NOT NULL,
  "personalizacao" text,
  "preco_unitario_registrado" decimal NOT NULL
);

-- ============================================================
-- ESTOQUE
-- Funciona como um "extrato bancário": cada linha aqui é um movimento.
-- O saldo atual (insumo.quantidade_estoque) é sempre a soma dessas linhas —
-- nunca é editado direto.
-- ============================================================

CREATE TABLE "movimentacao_estoque" (
  "id" uuid PRIMARY KEY,
  "insumo_id" uuid NOT NULL,
  "tipo" tipo_movimentacao_estoque NOT NULL,
  "quantidade" decimal NOT NULL,
  "origem" origem_movimentacao_estoque NOT NULL,
  "pedido_id" uuid,
  "fornecedor_insumo_id" uuid,
  "criado_em" timestamp NOT NULL
);

-- Deixa mais rápido buscar "o que já comprei desse fornecedor/insumo"
CREATE INDEX ON "fornecedor_insumo" ("fornecedor_id", "insumo_id");

-- ============================================================
-- NOTAS EXPLICATIVAS (aparecem no DBeaver/pgAdmin ao passar o mouse na coluna)
-- ============================================================

COMMENT ON TABLE "tom_cor" IS 'Cadastro livre pela administradora — não é enum.';

COMMENT ON COLUMN "insumo"."quantidade_estoque" IS 'Saldo materializado — derivado do ledger. Nunca editar direto.';

COMMENT ON COLUMN "fornecedor"."contato" IS 'Informal — PF comprando de loja de bairro, sem CNPJ.';

COMMENT ON COLUMN "cliente"."whatsapp" IS 'Identificador de login; link de verificação vai por e-mail.';

COMMENT ON COLUMN "cliente"."email" IS 'Nullable: sem e-mail, cliente não consegue logar via magic link.';

COMMENT ON COLUMN "cliente"."email_verificado_em" IS 'Preenchido no 1º clique de magic link bem-sucedido; nulo se e-mail for trocado.';

COMMENT ON COLUMN "cliente_login_token"."expira_em" IS 'Curto, ex: 15 min.';

COMMENT ON COLUMN "cliente_login_token"."usado_em" IS 'Nulo até o clique — token de uso único.';

COMMENT ON COLUMN "pedido"."data_evento_solicitada" IS 'O que o cliente pediu originalmente.';

COMMENT ON COLUMN "pedido"."data_evento_confirmada" IS 'Preenchido só quando status vira AGENDADO — nunca nulo nesse status.';

COMMENT ON COLUMN "pedido"."valor_estimado_inicial" IS 'Calculado no monte sua encomenda — nunca sobrescrito.';

COMMENT ON COLUMN "pedido"."valor_confirmado" IS 'Preenchido na negociação — nullable até a confirmação.';

COMMENT ON COLUMN "pedido_status_historico"."alterado_por" IS 'Nulo em transições automáticas, ex: cancelamento por timeout.';

COMMENT ON COLUMN "pedido_status_historico"."motivo" IS 'Opcional, mas obrigatório na prática para CANCELADO.';

COMMENT ON COLUMN "item_pedido"."preco_unitario_registrado" IS 'Snapshot no momento do pedido — produto.preco_unitario muda com o tempo.';

COMMENT ON COLUMN "movimentacao_estoque"."pedido_id" IS 'Preenchido quando origem = PRODUCAO_PEDIDO.';

COMMENT ON COLUMN "movimentacao_estoque"."fornecedor_insumo_id" IS 'Preenchido quando origem = COMPRA_FORNECEDOR.';

-- ============================================================
-- LIGAÇÕES ENTRE TABELAS (chaves estrangeiras / FKs)
-- Cada linha abaixo diz "esse campo aqui só pode apontar pra um
-- registro que já existe na outra tabela" — impede pedido órfão,
-- item de pedido sem produto, etc.
-- ============================================================

ALTER TABLE "produto" ADD FOREIGN KEY ("tom_cor_id") REFERENCES "tom_cor" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "ficha_tecnica" ADD FOREIGN KEY ("produto_id") REFERENCES "produto" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "ficha_tecnica" ADD FOREIGN KEY ("insumo_id") REFERENCES "insumo" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "fornecedor_insumo" ADD FOREIGN KEY ("fornecedor_id") REFERENCES "fornecedor" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "fornecedor_insumo" ADD FOREIGN KEY ("insumo_id") REFERENCES "insumo" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "cliente_login_token" ADD FOREIGN KEY ("cliente_id") REFERENCES "cliente" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pedido" ADD FOREIGN KEY ("cliente_id") REFERENCES "cliente" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pedido_status_historico" ADD FOREIGN KEY ("pedido_id") REFERENCES "pedido" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pedido_status_historico" ADD FOREIGN KEY ("alterado_por") REFERENCES "usuario_admin" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "item_pedido" ADD FOREIGN KEY ("pedido_id") REFERENCES "pedido" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "item_pedido" ADD FOREIGN KEY ("produto_id") REFERENCES "produto" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "movimentacao_estoque" ADD FOREIGN KEY ("insumo_id") REFERENCES "insumo" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "movimentacao_estoque" ADD FOREIGN KEY ("pedido_id") REFERENCES "pedido" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "movimentacao_estoque" ADD FOREIGN KEY ("fornecedor_insumo_id") REFERENCES "fornecedor_insumo" ("id") DEFERRABLE INITIALLY IMMEDIATE;

-- ============================================================
-- REGRA DE CONSISTÊNCIA DO ESTOQUE (adicionada manualmente)
-- O dbdiagram.io não sabe modelar essa regra, por isso não veio
-- na exportação — teve que ser escrita à mão aqui.
--
-- O que ela garante: uma movimentação de estoque só pode apontar
-- pra UM motivo de cada vez.
--   • Se veio de produção de um pedido      -> só pedido_id preenchido
--   • Se veio de compra de fornecedor       -> só fornecedor_insumo_id preenchido
--   • Se foi um ajuste manual               -> nenhum dos dois preenchido
-- Sem essa trava, dava pra criar um registro contraditório, tipo
-- "veio de fornecedor" mas com pedido_id preenchido — o que corromperia
-- o extrato de estoque sem ninguém perceber na hora.
-- ============================================================

ALTER TABLE "movimentacao_estoque" ADD CONSTRAINT "chk_movimentacao_origem"
CHECK (
  (origem = 'PRODUCAO_PEDIDO'   AND pedido_id IS NOT NULL AND fornecedor_insumo_id IS NULL) OR
  (origem = 'COMPRA_FORNECEDOR' AND fornecedor_insumo_id IS NOT NULL AND pedido_id IS NULL) OR
  (origem = 'AJUSTE_MANUAL'     AND pedido_id IS NULL AND fornecedor_insumo_id IS NULL)
);
