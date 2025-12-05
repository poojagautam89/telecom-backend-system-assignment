-- ===========================================
-- V5: Monthly Reporting Summary VIEW
-- ===========================================

CREATE OR REPLACE VIEW reporting_monthly_summary AS
SELECT
    s.sim_id AS sim_id,
    s.msisdn AS msisdn,
    s.subscriber_id AS subscriber_id,
    b.billing_month AS billing_month,                    -- format: 'YYYY-MM'

    -- plan info
    p.plan_id AS plan_id,
    p.plan_name AS plan_name,
    p.monthly_price AS base_plan_price,

    -- usage totals
    COALESCE((
        SELECT SUM(u.usage_value)
        FROM usage_record u
        WHERE u.sim_id = s.sim_id
        AND u.usage_type = 'DATA'
        AND to_char(u.usage_timestamp, 'YYYY-MM') = b.billing_month
    ), 0) AS total_data_kb,

    COALESCE((
        SELECT SUM(u.usage_value)
        FROM usage_record u
        WHERE u.sim_id = s.sim_id
        AND u.usage_type = 'CALL'
        AND to_char(u.usage_timestamp, 'YYYY-MM') = b.billing_month
    ), 0) AS total_call_min,

    COALESCE((
        SELECT SUM(u.usage_value)
        FROM usage_record u
        WHERE u.sim_id = s.sim_id
        AND u.usage_type = 'SMS'
        AND to_char(u.usage_timestamp, 'YYYY-MM') = b.billing_month
    ), 0) AS total_sms_count,

    -- billing data
    b.base_amount,
    b.overage_amount,
    b.total_amount,

    now() AS generated_at

FROM sims s
LEFT JOIN bill b ON b.sim_id = s.sim_id
LEFT JOIN plans p ON p.plan_id = s.plan_id

WHERE b.billing_month IS NOT NULL;
