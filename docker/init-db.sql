-- Create flights table if it doesn't exist
CREATE TABLE IF NOT EXISTS flights (
    id BIGSERIAL PRIMARY KEY,
    flight_number VARCHAR(255) NOT NULL,
    origin VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    departure_date_time TIMESTAMP NOT NULL,
    duration_minutes BIGINT NOT NULL,
    airline VARCHAR(255) NOT NULL
);

-- Insert sample data only if table is empty
INSERT INTO flights (flight_number, origin, destination, departure_date_time, duration_minutes, airline)
SELECT
    flight_number,
    origin,
    destination,
    departure_date_time::TIMESTAMP,
    duration_minutes,
    airline
FROM (VALUES
    ('AA100', 'JFK', 'LAX', '2025-10-22 10:30:00', 330, 'American Airlines'),
    ('AA101', 'JFK', 'SFO', '2025-10-22 14:15:00', 360, 'American Airlines'),
    ('UA200', 'LAX', 'SFO', '2025-10-22 08:00:00', 90, 'United Airlines'),
    ('UA201', 'LAX', 'ORD', '2025-10-22 12:30:00', 240, 'United Airlines'),
    ('DL300', 'SFO', 'JFK', '2025-10-23 09:00:00', 330, 'Delta Airlines'),
    ('DL301', 'SFO', 'ATL', '2025-10-23 11:45:00', 270, 'Delta Airlines'),
    ('SW400', 'ORD', 'DEN', '2025-10-23 07:30:00', 150, 'Southwest Airlines'),
    ('SW401', 'ORD', 'LAS', '2025-10-23 16:00:00', 210, 'Southwest Airlines'),
    ('BA500', 'JFK', 'LHR', '2025-10-24 18:00:00', 420, 'British Airways'),
    ('BA501', 'JFK', 'CDG', '2025-10-24 20:30:00', 450, 'British Airways')
) AS v(flight_number, origin, destination, departure_date_time, duration_minutes, airline)
WHERE NOT EXISTS (SELECT 1 FROM flights LIMIT 1);

-- Create index on origin for faster searches
CREATE INDEX IF NOT EXISTS idx_flights_origin ON flights(origin);

-- Display inserted data count
SELECT 'Database initialized with ' || COUNT(*) || ' flights' AS status FROM flights;
