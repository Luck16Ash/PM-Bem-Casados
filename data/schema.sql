CREATE TYPE "status_pedido" AS ENUM (
  'SOLICITACAO',
  'ANALISE_NEGOCIACAO',
  'CONFIRMACAO',
  'AGENDADO',
  'PRONTO',
  'ENTREGUE',
  'CANCELADO'
);

CREATE TYPE "tipo_movimentacao_estoque" AS ENUM (
  'ENTRADA',
  'SAIDA',
  'AJUSTE'
);

CREATE TYPE "origem_movimentacao_estoque" AS ENUM (
  'COMPRA_FORNECEDOR',
  'PRODUCAO_PEDIDO',
  'AJUSTE_MANUAL'
);

CREATE TYPE "regiao_fornecedor" AS ENUM (
  'ZONA_SUL',
  'ZONA_LESTE',
  'ZONA_OESTE',
  'ZONA_NORTE'
);

CREATE TABLE "usuario_admin" (
  "id" uuid PRIMARY KEY,
  "nome" varchar NOT NULL,
  "email" varchar UNIQUE NOT NULL,
  "senha_hash" varchar NOT NULL,
  "criado_em" timestamp NOT NULL
);

CREATE TABLE "tom_cor" (
  "id" uuid PRIMARY KEY,
  "nome" varchar UNIQUE NOT NULL,
  "ativo" boolean NOT NULL,
  "criado_em" timestamp NOT NULL
);

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

CREATE TABLE "insumo" (
  "id" uuid PRIMARY KEY,
  "nome" varchar NOT NULL,
  "unidade_medida" varchar NOT NULL,
  "quantidade_estoque" decimal NOT NULL,
  "quantidade_minima" decimal NOT NULL
);

CREATE TABLE "ficha_tecnica" (
  "produto_id" uuid NOT NULL,
  "insumo_id" uuid NOT NULL,
  "quantidade_necessaria" decimal NOT NULL,
  PRIMARY KEY ("produto_id", "insumo_id")
);

CREATE TABLE "fornecedor" (
  "id" uuid PRIMARY KEY,
  "nome" varchar NOT NULL,
  "bairro" varchar,
  "regiao" regiao_fornecedor,
  "contato" varchar
);

CREATE TABLE "fornecedor_insumo" (
  "id" uuid PRIMARY KEY,
  "fornecedor_id" uuid NOT NULL,
  "insumo_id" uuid NOT NULL,
  "ultimo_preco" decimal,
  "ultima_compra_em" date
);

CREATE TABLE "cliente" (
  "id" uuid PRIMARY KEY,
  "nome" varchar NOT NULL,
  "whatsapp" varchar UNIQUE NOT NULL,
  "email" varchar UNIQUE,
  "email_verificado_em" timestamp,
  "criado_em" timestamp NOT NULL,
  "consentimento_lgpd_em" timestamp NOT NULL
);

CREATE TABLE "cliente_login_token" (
  "id" uuid PRIMARY KEY,
  "cliente_id" uuid NOT NULL,
  "token" uuid UNIQUE NOT NULL,
  "expira_em" timestamp NOT NULL,
  "usado_em" timestamp,
  "criado_em" timestamp NOT NULL
);

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

CREATE TABLE "pedido_status_historico" (
  "id" uuid PRIMARY KEY,
  "pedido_id" uuid NOT NULL,
  "status" status_pedido NOT NULL,
  "alterado_por" uuid,
  "motivo" text,
  "alterado_em" timestamp NOT NULL
);

CREATE TABLE "item_pedido" (
  "id" uuid PRIMARY KEY,
  "pedido_id" uuid NOT NULL,
  "produto_id" uuid NOT NULL,
  "quantidade" int NOT NULL,
  "personalizacao" text,
  "preco_unitario_registrado" decimal NOT NULL
);

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

CREATE INDEX ON "fornecedor_insumo" ("fornecedor_id", "insumo_id");

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

-- Adicionado manualmente: dbdiagram.io não modela CHECK constraints.
-- Amarra origem <-> par de FKs mutuamente exclusivo em movimentacao_estoque.
ALTER TABLE "movimentacao_estoque" ADD CONSTRAINT "chk_movimentacao_origem"
CHECK (
  (origem = 'PRODUCAO_PEDIDO'   AND pedido_id IS NOT NULL AND fornecedor_insumo_id IS NULL) OR
  (origem = 'COMPRA_FORNECEDOR' AND fornecedor_insumo_id IS NOT NULL AND pedido_id IS NULL) OR
  (origem = 'AJUSTE_MANUAL'     AND pedido_id IS NULL AND fornecedor_insumo_id IS NULL)
);
