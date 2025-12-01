-- V3__create_usage_and_bill.sql
-- Usage and Bill tables (aligned with sims.sim_id UUID)

-- NOTE: remove or modify if plan/sims already created in earlier migrations

CREATE TABLE IF NOT EXISTS usage_record (
  id BIGSERIAL PRIMARY KEY,
  sim_id UUID NOT NULL,
  usage_type VARCHAR(16) NOT NULL, -- CALL/DATA/SMS
  usage_value BIGINT NOT NULL, -- value in KB for DATA, minutes for CALL, count for SMS
  usage_timestamp TIMESTAMP NOT NULL,
  created_at TIMESTAMP DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_usage_sim_ts ON usage_record(sim_id, usage_timestamp);

CREATE TABLE IF NOT EXISTS bill (
  id BIGSERIAL PRIMARY KEY,
  sim_id UUID NOT NULL,
  billing_month VARCHAR(7) NOT NULL, -- YYYY-MM
  base_amount NUMERIC(12,2) DEFAULT 0,
  overage_amount NUMERIC(12,2) DEFAULT 0,
  total_amount NUMERIC(12,2) DEFAULT 0,
  generated_at TIMESTAMP DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_bill_sim_month ON bill(sim_id, billing_month);

-- FKs (ensure sims table exists and has sim_id UUID PK)
ALTER TABLE usage_record
  ADD CONSTRAINT fk_usage_record_sim FOREIGN KEY (sim_id) REFERENCES sims(sim_id);

ALTER TABLE bill
  ADD CONSTRAINT fk_bill_sim FOREIGN KEY (sim_id) REFERENCES sims(sim_id);
