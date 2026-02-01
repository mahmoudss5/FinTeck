use azbanking;
CREATE TABLE IF NOT EXISTS support_ticket_response
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id        BIGINT NOT NULL,
    sender_id        BIGINT NOT NULL,
    response_message TEXT   NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_response_ticket FOREIGN KEY (ticket_id) REFERENCES support_ticket (id),
    CONSTRAINT fk_response_sender FOREIGN KEY (sender_id) REFERENCES users (id)
);

