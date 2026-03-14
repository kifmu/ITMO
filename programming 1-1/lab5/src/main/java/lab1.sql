DROP TABLE IF EXISTS location_safety CASCADE;
DROP TABLE IF EXISTS task_executions CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS robots CASCADE;
DROP TABLE IF EXISTS commands CASCADE;
DROP TABLE IF EXISTS inner_creature CASCADE;
DROP TABLE IF EXISTS internal_conflicts CASCADE;
DROP TABLE IF EXISTS physical_states CASCADE;
DROP TABLE IF EXISTS humans CASCADE;
DROP TABLE IF EXISTS emotional_states CASCADE;

CREATE TABLE humans (id SERIAL PRIMARY KEY, name VARCHAR(30));
CREATE TABLE physical_states (id SERIAL PRIMARY KEY, human_id INT REFERENCES humans(id), description TEXT);
CREATE TABLE emotional_states (id SERIAL PRIMARY KEY, human_id INT REFERENCES humans(id), emotion_type TEXT, intensity INT);
CREATE TABLE inner_creature (id SERIAL PRIMARY KEY, human_id INT REFERENCES humans(id), description TEXT);
CREATE TABLE internal_conflicts (id SERIAL PRIMARY KEY, human_id INT REFERENCES humans(id), creature_id INT REFERENCES inner_creature(id), conflict_details TEXT);
CREATE TABLE commands (id SERIAL PRIMARY KEY, human_id INT REFERENCES humans(id), command_type TEXT, content TEXT);
CREATE TABLE locations (id SERIAL PRIMARY KEY, name VARCHAR(30), coordinates POINT);
CREATE TABLE robots (id SERIAL PRIMARY KEY, characteristic_type TEXT, current_location_id INT REFERENCES locations(id));
CREATE TABLE task_executions (id SERIAL PRIMARY KEY, executor_id INT REFERENCES robots(id), command_id INT REFERENCES commands(id), status TEXT);
CREATE TABLE location_safety (id SERIAL PRIMARY KEY, location_id INT REFERENCES locations(id), safety_level INT);

INSERT INTO humans(name) VALUES ('Олвин');

INSERT INTO physical_states(human_id, description) VALUES
                                                           (1, 'перехватило дыхание'),
                                                           (1, 'вяло сопротивляясь тем силам, которых, он знал, ему не преодолеть');

INSERT INTO emotional_states(human_id, emotion_type, intensity) VALUES
                                                                        (1, 'мольба', 20),
                                                                        (1, 'смирение', 80),
                                                                        (1, 'азарт', 100),
                                                                        (1, 'уверенность', 0);

INSERT INTO inner_creature(human_id, description) VALUES
    (1, 'Различная сущность, умоляющая отпустить на землю');

INSERT INTO internal_conflicts(human_id, creature_id, conflict_details) VALUES
    (1, 1, 'В его мозгу боролись теперь две совершенно различные сущности...');

INSERT INTO commands(human_id, command_type, content) VALUES
                                                              (1, 'жесткая', 'Не повиноваться его же командам, пока не окажется в Диаспаре'),
                                                              (1, 'сложная', 'Приказы, которые изначально отдал Олвин');

INSERT INTO locations(name, coordinates) VALUES
    ('Диаспар', POINT(1.0, 2.0));

INSERT INTO robots(characteristic_type, current_location_id) VALUES
    ('недружелюбный', 1);

INSERT INTO task_executions(executor_id, command_id, status) VALUES
                                                                     (1, 1, 'выполнен'),
                                                                     (1, 2, 'выполняется');

INSERT INTO location_safety(location_id, safety_level) VALUES
    (1, 100);