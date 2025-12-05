CREATE TABLE IF NOT EXISTS payments (
  payment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  bill_id BIGINT NOT NULL,
  subscriber_id UUID,
  amount NUMERIC(12,2) NOT NULL,
  mode VARCHAR(20) NOT NULL,
  transaction_id VARCHAR(128),
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  paid_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT now(),
  CONSTRAINT fk_payment_bill FOREIGN KEY (bill_id) REFERENCES bill(id),
  CONSTRAINT fk_payment_subscriber FOREIGN KEY (subscriber_id) REFERENCES subscribers(subscriber_id)
);

CREATE INDEX IF NOT EXISTS idx_payments_bill_id ON payments(bill_id);
CREATE INDEX IF NOT EXISTS idx_payments_subscriber_id ON payments(subscriber_id);
