DROP TABLE IF EXISTS "client" CASCADE;
DROP TABLE IF EXISTS "categorie_list" CASCADE;
DROP TABLE IF EXISTS "categorie" CASCADE;
DROP TABLE IF EXISTS "budget" CASCADE;
DROP TABLE IF EXISTS "budget_categorie" CASCADE;

CREATE TABLE IF NOT EXISTS "client"(
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "categorie_list"(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- CREATE TABLE "categorie" (
--     id SERIAL PRIMARY KEY,
--     client_id INT NOT NULL REFERENCES client(id) ON DELETE CASCADE,
--     categorieList_id VARCHAR(100) NOT NULL,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- Budget du mois 
CREATE TABLE "budget" (
    id SERIAL PRIMARY KEY,
    client_id INT NOT NULL REFERENCES client(id) ON DELETE CASCADE,
    month INT NOT NULL CHECK (month BETWEEN 1 AND 12),
    montant_total DECIMAL(12,2) NOT NULL CHECK (montant_total > 0),  -- Budget global du mois
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(client_id, month)  -- Un seul budget par mois et par client
);

CREATE TABLE "budget_categorie" (
    id SERIAL PRIMARY KEY,
    budget_id INT NOT NULL REFERENCES budget(id) ON DELETE CASCADE,
    categorielist_id INT NOT NULL REFERENCES categorie_list(id) ON DELETE CASCADE,
    montant DECIMAL(12,2) NOT NULL CHECK (montant >= 0),  -- Montant alloué à cette catégorie
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(budget_id, categorieList_id)  -- Une catégorie ne peut apparaître qu'une seule fois dans un budget
);