CREATE TABLE IF NOT EXISTS Property (
	name TEXT UNIQUE NOT NULL,
	value TEXT NULL
);

CREATE TABLE IF NOT EXISTS Namespace (
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
	name TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS Item (
	ID INTEGER UNIQUE NOT NULL, -- Numeric ID of item
	ns INTEGER NOT NULL DEFAULT 0, -- Namespace. By default should be 'minecraft'
	nsID TEXT NOT NULL,
	name TEXT NOT NULL, -- Name/title of item
	icon TEXT NOT NULL, -- Name of icon associated with item
	FOREIGN KEY (ns) REFERENCES Namespace (ID)
);

INSERT INTO Namespace
	VALUES (0, 'minecraft');
INSERT INTO Item VALUES
	(2, 0, 'grass_block', 'Grass Block', 'grass_block.png');
