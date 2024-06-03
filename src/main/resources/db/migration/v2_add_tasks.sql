CREATE TABLE if not exists tw_task  (
                         id UUID PRIMARY KEY,
                         type TEXT NOT NULL,
                         sub_type TEXT NULL,
                         status TEXT NOT NULL,
                         data TEXT NOT NULL,
                         next_event_time TIMESTAMPTZ(6) NOT NULL,
                         state_time TIMESTAMPTZ(3) NOT NULL,
                         processing_client_id TEXT NULL,
                         processing_start_time TIMESTAMPTZ(3) NULL,
                         time_created TIMESTAMPTZ(3) NOT NULL,
                         time_updated TIMESTAMPTZ(3) NOT NULL,
                         processing_tries_count BIGINT NOT NULL,
                         version BIGINT NOT NULL,
                         priority INT NOT NULL DEFAULT 5
);

CREATE INDEX if not exists tw_task_idx1 ON tw_task (status, next_event_time);

CREATE TABLE if not exists tw_task_data (
                              task_id UUID PRIMARY KEY NOT NULL,
                              data_format INT NOT NULL,
                              data BYTEA NOT NULL
) WITH (toast_tuple_target=8160);

ALTER TABLE tw_task_data ALTER COLUMN data SET STORAGE EXTERNAL;

CREATE TABLE if not exists unique_tw_task_key (
                                    task_id UUID PRIMARY KEY NOT NULL,
                                    key_hash INT NOT NULL,
                                    key TEXT NOT NULL,
                                    unique (key_hash, key)
);