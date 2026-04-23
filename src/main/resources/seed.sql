-- ============================================
-- SEED DATA - TechStore Madagascar (Ariary)
-- ============================================

-- CATEGORIES
INSERT INTO categories (name, description) VALUES
('Ordinateurs Portables', 'Laptops et notebooks'),
('Ordinateurs de Bureau', 'PC fixes et tours'),
('Écrans', 'Moniteurs et écrans'),
('Composants', 'CPU, RAM, disques durs'),
('Périphériques', 'Claviers, souris, casques'),
('Réseaux', 'Routeurs, switches, câbles'),
('Accessoires', 'Sacs, housses, câbles divers')
ON CONFLICT (name) DO NOTHING;

-- ============================================
-- USERS (mot de passe: Admin1234! et User1234!)
-- BCrypt hash généré avec strength=10
-- ============================================
INSERT INTO users (username, email, password, first_name, last_name, role, is_active) VALUES
('admin', 'admin@techstore.mg', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Admin', 'TechStore', 'ADMIN', true),
('rakoto', 'rakoto@gmail.com',  '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Rakoto', 'Andriantsoa', 'USER', true),
('rabe',   'rabe@gmail.com',    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Rabe', 'Rasoa', 'USER', true)
ON CONFLICT (email) DO NOTHING;

-- ============================================
-- PRODUITS (prix en Ariary)
-- ============================================

-- Ordinateurs Portables
INSERT INTO products (name, description, price, image_url, stock_quantity, category_id) VALUES
('Laptop Lenovo IdeaPad 3',     'Intel Core i5, 8Go RAM, 512Go SSD, 15.6"',         2850000, 'https://placehold.co/400x300?text=Lenovo+IdeaPad3',  10, (SELECT id FROM categories WHERE name = 'Ordinateurs Portables')),
('Laptop HP 250 G9',            'Intel Core i3, 8Go RAM, 256Go SSD, 15.6"',         2200000, 'https://placehold.co/400x300?text=HP+250+G9',         8,  (SELECT id FROM categories WHERE name = 'Ordinateurs Portables')),
('Laptop ASUS VivoBook 15',     'AMD Ryzen 5, 16Go RAM, 512Go SSD, 15.6"',          3400000, 'https://placehold.co/400x300?text=ASUS+VivoBook15',   6,  (SELECT id FROM categories WHERE name = 'Ordinateurs Portables')),
('Laptop Dell Inspiron 15',     'Intel Core i7, 16Go RAM, 1To SSD, 15.6"',          4900000, 'https://placehold.co/400x300?text=Dell+Inspiron15',   4,  (SELECT id FROM categories WHERE name = 'Ordinateurs Portables')),
('Laptop Acer Aspire 5',        'Intel Core i5, 8Go RAM, 512Go SSD, 15.6"',         2650000, 'https://placehold.co/400x300?text=Acer+Aspire5',      7,  (SELECT id FROM categories WHERE name = 'Ordinateurs Portables'));

-- Ordinateurs de Bureau
INSERT INTO products (name, description, price, image_url, stock_quantity, category_id) VALUES
('PC Bureau HP ProDesk 400',    'Intel Core i5, 8Go RAM, 256Go SSD',                2100000, 'https://placehold.co/400x300?text=HP+ProDesk400',     5,  (SELECT id FROM categories WHERE name = 'Ordinateurs de Bureau')),
('PC Bureau Dell OptiPlex 3000','Intel Core i3, 4Go RAM, 1To HDD',                  1750000, 'https://placehold.co/400x300?text=Dell+OptiPlex3000', 6,  (SELECT id FROM categories WHERE name = 'Ordinateurs de Bureau')),
('PC Bureau Lenovo ThinkCentre','Intel Core i7, 16Go RAM, 512Go SSD',               3800000, 'https://placehold.co/400x300?text=Lenovo+ThinkCentre',3,  (SELECT id FROM categories WHERE name = 'Ordinateurs de Bureau'));

-- Écrans
INSERT INTO products (name, description, price, image_url, stock_quantity, category_id) VALUES
('Écran Samsung 24" FHD',       'Full HD 1080p, 75Hz, IPS, HDMI+VGA',               650000,  'https://placehold.co/400x300?text=Samsung+24FHD',     12, (SELECT id FROM categories WHERE name = 'Écrans')),
('Écran LG 27" QHD',            '2K 1440p, 144Hz, IPS, HDMI+DisplayPort',           1200000, 'https://placehold.co/400x300?text=LG+27QHD',          7,  (SELECT id FROM categories WHERE name = 'Écrans')),
('Écran Acer 21.5" FHD',        'Full HD 1080p, 60Hz, VA, HDMI+VGA',                480000,  'https://placehold.co/400x300?text=Acer+215FHD',       15, (SELECT id FROM categories WHERE name = 'Écrans'));

-- Composants
INSERT INTO products (name, description, price, image_url, stock_quantity, category_id) VALUES
('RAM DDR4 8Go Kingston',       'DDR4 3200MHz, compatible laptop et desktop',        180000,  'https://placehold.co/400x300?text=RAM+8Go+Kingston',  20, (SELECT id FROM categories WHERE name = 'Composants')),
('RAM DDR4 16Go Corsair',       'DDR4 3200MHz, kit 2x8Go',                           340000,  'https://placehold.co/400x300?text=RAM+16Go+Corsair',  15, (SELECT id FROM categories WHERE name = 'Composants')),
('SSD 512Go Samsung 870 EVO',   'SATA III, lecture 560Mo/s',                         380000,  'https://placehold.co/400x300?text=SSD+512Go+Samsung', 18, (SELECT id FROM categories WHERE name = 'Composants')),
('SSD NVMe 1To WD Black',       'PCIe Gen3, lecture 3470Mo/s',                       620000,  'https://placehold.co/400x300?text=SSD+1To+WD',        10, (SELECT id FROM categories WHERE name = 'Composants')),
('Disque Dur 1To Seagate',      'HDD 3.5", 7200RPM, SATA III',                       195000,  'https://placehold.co/400x300?text=HDD+1To+Seagate',   25, (SELECT id FROM categories WHERE name = 'Composants'));

-- Périphériques
INSERT INTO products (name, description, price, image_url, stock_quantity, category_id) VALUES
('Clavier Logitech K120',       'Filaire USB, AZERTY, résistant aux éclaboussures',  65000,   'https://placehold.co/400x300?text=Clavier+K120',      30, (SELECT id FROM categories WHERE name = 'Périphériques')),
('Souris Logitech M100',        'Filaire USB, 1000 DPI, ambidextre',                 45000,   'https://placehold.co/400x300?text=Souris+M100',       35, (SELECT id FROM categories WHERE name = 'Périphériques')),
('Combo Clavier+Souris sans fil','Logitech MK270, 2.4GHz, portée 10m',              145000,  'https://placehold.co/400x300?text=Combo+MK270',       20, (SELECT id FROM categories WHERE name = 'Périphériques')),
('Casque Gaming Redragon H510', 'Son surround 7.1, micro amovible, USB',             280000,  'https://placehold.co/400x300?text=Casque+H510',       12, (SELECT id FROM categories WHERE name = 'Périphériques')),
('Webcam Logitech C270',        'HD 720p, micro intégré, USB',                       195000,  'https://placehold.co/400x300?text=Webcam+C270',       14, (SELECT id FROM categories WHERE name = 'Périphériques'));

-- Réseaux
INSERT INTO products (name, description, price, image_url, stock_quantity, category_id) VALUES
('Routeur TP-Link AC1200',      'WiFi Dual Band, 4 ports LAN, 300Mbps+867Mbps',     195000,  'https://placehold.co/400x300?text=Routeur+AC1200',    10, (SELECT id FROM categories WHERE name = 'Réseaux')),
('Switch TP-Link 8 ports',      'Gigabit non manageable, plug & play',               120000,  'https://placehold.co/400x300?text=Switch+8ports',     8,  (SELECT id FROM categories WHERE name = 'Réseaux')),
('Câble RJ45 Cat6 (10m)',       'FTP blindé, 1Gbps, gaine PVC',                      25000,   'https://placehold.co/400x300?text=Cable+RJ45+10m',    50, (SELECT id FROM categories WHERE name = 'Réseaux'));

-- Accessoires
INSERT INTO products (name, description, price, image_url, stock_quantity, category_id) VALUES
('Sac Laptop 15.6" Targus',     'Imperméable, compartiments multiples, bandoulière', 185000,  'https://placehold.co/400x300?text=Sac+Targus+156',    15, (SELECT id FROM categories WHERE name = 'Accessoires')),
('Tapis de souris XXL',         '800x300mm, surface lisse, base antidérapante',      35000,   'https://placehold.co/400x300?text=Tapis+XXL',         25, (SELECT id FROM categories WHERE name = 'Accessoires')),
('Hub USB 4 ports',             'USB 3.0, compatible Windows/Mac/Linux',             75000,   'https://placehold.co/400x300?text=Hub+USB4',          20, (SELECT id FROM categories WHERE name = 'Accessoires')),
('Multiprise 6 prises parasurtenseur', '6 prises + 2 USB, câble 1.5m, 16A',         95000,   'https://placehold.co/400x300?text=Multiprise+6P',     18, (SELECT id FROM categories WHERE name = 'Accessoires'));
