-- V2__create_plans_and_sims.sql
-- Ensure pgcrypto for gen_random_uuid() if you want DB-side UUID generation
-- CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Create PLANS table matching Plan entity
CREATE TABLE IF NOT EXISTS plans (
  plan_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  plan_name VARCHAR(100) NOT NULL,
  plan_type VARCHAR(20) NOT NULL,
  monthly_price NUMERIC(12,2) NOT NULL DEFAULT 0,
  data_allowance_mb BIGINT NOT NULL DEFAULT 0,
  call_allowance_min BIGINT NOT NULL DEFAULT 0,
  sms_allowance BIGINT NOT NULL DEFAULT 0,
  created_at TIMESTAMP DEFAULT now()
);

-- Create SIMS table matching Sim entity
CREATE TABLE IF NOT EXISTS sims (
  sim_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  imsi VARCHAR(64) NOT NULL UNIQUE,
  msisdn VARCHAR(32) UNIQUE,
  subscriber_id UUID NOT NULL,
  plan_id UUID,
  status VARCHAR(16) NOT NULL DEFAULT 'INACTIVE',
  activation_date DATE,
  created_at TIMESTAMP DEFAULT now()
);

-- Foreign keys
ALTER TABLE sims
  ADD CONSTRAINT fk_sims_subscriber FOREIGN KEY (subscriber_id) REFERENCES subscribers(subscriber_id);

ALTER TABLE sims
  ADD CONSTRAINT fk_sims_plan FOREIGN KEY (plan_id) REFERENCES plans(plan_id);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_sims_subscriber_id ON sims(subscriber_id);
CREATE INDEX IF NOT EXISTS idx_sims_status ON sims(status);
