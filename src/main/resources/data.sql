-- Vaciar las tablas en orden correcto para evitar errores de clave foránea
TRUNCATE TABLE public.user_roles CASCADE;
TRUNCATE TABLE public.users CASCADE;
TRUNCATE TABLE public.roles CASCADE;
TRUNCATE TABLE public.vehiculos CASCADE;

-- Insert roles
INSERT INTO public.roles (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO public.roles (id, name) VALUES (2, 'ROLE_ADMIN');

-- Insert users
INSERT INTO public.users (id, apellidos, codigo_postal, email, fecha_nacimiento, nombre, password, telefono) VALUES
(1, 'gotor', '08490', 'quimontilivi@gmail.com', '1994-02-12', 'quim', '$2a$10$cj4aOHWIK56ennLnM4P0Au8MIn10td6M7E6b3PjdFwZ05Frwvz5Wi', '639008205'),
(2, 'deere smith', '55588', 'bkl81652@jioso.com', '1888-04-23', 'john', '$2a$10$HMBoWQxCvc4nLkcAPxzhwuCc2EYCllIncbtODncLL8V.jGfaa49NW', '458798545'),
(3, 'deere smith', '789540', 'varah44087@magpit.com', '1880-02-20', 'john', '$2a$10$WiPaQPF18YgdYuLE9Vb15eNgHtfkzYIbc9ZvgTBaPNCXCkOzLv9G2', '684875985'),
(4, 'deere smith', '879540', 'jxl76053@jioso.com', '1880-02-20', 'john', '$2a$10$YOesk8Up1QQizyYii9rpZuZXvTdG7Ix6rkQAUWnBAuCx0AtCCtMs6', '789456123'),
(5, 'deere smith', '78945', 'MS_PvBZPS@test-eqvygm006n5l0p7w.mlsender.net', '1999-10-10', 'john', '$2a$10$gagZ6hNoEqGeJO7yl.OO/u1XIqCqulCLvjhEvSNsX25emgXyWvSO.', '789456123'),
(6, 'deere smith', '78945', 'tci21476@toaik.com', '1880-08-08', 'john', '$2a$10$iqJJOPZmXq5vDJGAI/tDj.EcZjoKz9Ygy0y8ksyk/UvHwbPk09Ga.', '789456123'),
(7, 'deere smith', '45621', 'ventacar@gmail.com', '1880-12-12', 'john', '$2a$10$aX4H9WBgH5UNbSPEZGbFHeHcvms2FPC22Dm.gSz7Ygr2GFy4agKym', '654254451'),
(8, 'deere smith', '78548', 'solrak_27@hotmail.com', '1990-10-10', 'john', '$2a$10$LeNBzCDsWLZ2CabrKA2XreYNmkqTRG9mZpg4uti32GLoYsJOkSpR2', '78458754');

-- Insert user_roles
INSERT INTO public.user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO public.user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO public.user_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO public.user_roles (user_id, role_id) VALUES (3, 1);
INSERT INTO public.user_roles (user_id, role_id) VALUES (4, 1);
INSERT INTO public.user_roles (user_id, role_id) VALUES (5, 1);
INSERT INTO public.user_roles (user_id, role_id) VALUES (6, 1);
INSERT INTO public.user_roles (user_id, role_id) VALUES (8, 1);

-- Insert vehiculos
INSERT INTO public.vehiculos (id, marca, tipo, precio, imagen, descripcion) VALUES
(1, 'BMW', 'SUV', 34990, 'BMW_SUV_1.png', NULL),
(2, 'BMW', 'Berlina', 44990, 'BMW_BERLINA.png', NULL),
(3, 'BMW', 'SUV', 49990, 'BMW_SUV_2.png', NULL),
(4, 'BMW', '4x4', 45990, 'BMW_4X4.png', NULL),
(5, 'Audi', 'SUV', 38990, 'AUDI_SUV_1.png', NULL),
(6, 'Audi', 'Berlina', 52990, 'AUDI_BERLINA_1.png', NULL),
(7, 'Audi', 'SUV', 45990, 'AUDI_SUV_2.png', NULL),
(8, 'Audi', 'Berlina', 54990, 'AUDI_BERLINA_2.png', NULL),
(9, 'Mercedes', 'SUV', 54990, 'MERCEDES_SUV_1.png', NULL),
(10, 'Mercedes', 'Berlina', 59990, 'MERCEDES_BERLINA.png', NULL),
(11, 'Mercedes', 'SUV', 79990, 'MERCEDES_SUV_2.png', NULL),
(12, 'Mercedes', '4x4', 69990, 'MERCEDES_4X4.png', NULL),
(13, 'Ford', 'SUV', 24990, 'FORD_SUV_1.png', NULL),
(14, 'Ford', 'Berlina', 21990, 'FORD_BERLINA.png', NULL),
(15, 'Ford', 'SUV', 27990, 'FORD_SUV_2.png', NULL),
(16, 'Ford', '4x4', 31990, 'FORD_4X4.png', NULL),
(17, 'Toyota', 'SUV', 27990, 'TOYOTA_SUV_1.png', NULL),
(18, 'Toyota', '4x4', 31990, 'TOYOTA_4X4.png', NULL),
(19, 'Toyota', 'Berlina', 24990, 'TOYOTA_BERLINA.png', NULL),
(20, 'Toyota', 'SUV', 35990, 'TOYOTA_SUV_2.png', NULL),
(21, 'Hyundai', 'SUV', 27990, 'HYUNDAI_SUV_1.png', NULL),
(22, 'Hyundai', 'Berlina', 22990, 'HYUNDAI_BERLINA.png', NULL),
(23, 'Hyundai', 'SUV', 31990, 'HYUNDAI_SUV_2.png', NULL),
(24, 'Hyundai', '4x4', 33990, 'HYUNDAI_4X4.png', NULL),
(25, 'Mercedes', 'Turismo', 35650, 'MERCEDES_TURISMO.png', NULL),
(26, 'Hyundai', 'Turismo', 23500, 'HYUNDAI_TURISMO.png', NULL),
(27, 'Ford', 'Turismo', 22900, 'FORD_TURISMO.png', NULL),
(28, 'Toyota', 'Turismo', 21550, 'TOYOTA_TURISMO.png', NULL),
(29, 'Audi', 'Turismo', 23850, 'AUDI_TURISMO.png', NULL),
(30, 'BMW', 'Turismo', 25990, 'BMW_TURISMO.png', NULL);
