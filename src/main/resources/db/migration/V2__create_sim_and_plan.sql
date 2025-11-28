-- V2__create_sim_and_plan.sql
-- Sprint-2: create plan and sim tables (matching client needs)

-- OPTIONAL: enable pgcrypto for gen_random_uuid()
-- CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Create PLAN table first
CREATE TABLE IF NOT EXISTS plan (
  plan_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  plan_name TEXT NOT NULL,
  data_limit_per_day DOUBLE PRECISION,
  validity_days INTEGER,
  sms_limit_per_day INTEGER,
  price NUMERIC(12,2),
  plan_type TEXT,
  created_at TIMESTAMP DEFAULT now()
);

-- Create SIM table (references subscribers and plan)
CREATE TABLE IF NOT EXISTS sim (
  sim_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  subscriber_id UUID NOT NULL,
  sim_number TEXT UNIQUE NOT NULL,
  imei_number TEXT,
  status TEXT NOT NULL,
  assigned_plan_id UUID,
  activation_date DATE,
  created_at TIMESTAMP DEFAULT now(),
  CONSTRAINT fk_sim_subscriber FOREIGN KEY (subscriber_id) REFERENCES subscribers(subscriber_id),
  CONSTRAINT fk_sim_plan FOREIGN KEY (assigned_plan_id) REFERENCES plan(plan_id)
);

-- Useful indexes
CREATE INDEX IF NOT EXISTS idx_sim_subscriber_id ON sim(subscriber_id);
CREATE INDEX IF NOT EXISTS idx_sim_status ON sim(status);
