
CREATE TABLE IF NOT EXISTS users.userdataset (
    id SERIAL PRIMARY KEY,            
    name VARCHAR(100),                
    cpf VARCHAR(11),                  
    address VARCHAR(100),             
    email VARCHAR(100),               
    phone VARCHAR(100),               
    register TIMESTAMP NOT NULL,      
    access_key VARCHAR(36)            
);
