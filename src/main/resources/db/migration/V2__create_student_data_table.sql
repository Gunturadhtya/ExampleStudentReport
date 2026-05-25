
CREATE TABLE IF NOT EXISTS student_data (
    "id" UUID NOT NULL,
    "user_id" UUID NOT NULL UNIQUE,
    "nim" VARCHAR(16) NOT NULL,
    "faculty" VARCHAR(255) NOT NULL,
    "major" VARCHAR(255) NOT NULL,
    "year" INTEGER NOT NULL,
    PRIMARY KEY("id")
);

ALTER TABLE student_data
    ADD FOREIGN KEY("user_id") REFERENCES users("id") ON UPDATE NO ACTION ON DELETE CASCADE;