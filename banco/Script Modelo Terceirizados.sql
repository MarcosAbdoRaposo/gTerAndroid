create database dbGter
go


/* Modelo Terceirizados: */

CREATE TABLE Terceirizado (
    ideTerceirizado UniqueIdentifier not null,
    nomTerceirizado VARCHAR(50),
    numCpf VARCHAR(11),
    datNascimento datetime,
    codSexo char(1),
    nomLogradouro varchar(60),
    nomBairro varchar(36),
    nomCidade varchar(36),
    sigUF varchar(2),
    nomPais varchar(48),
    numCEP varchar(10)
);
 
ALTER TABLE Terceirizado ADD CONSTRAINT PK_Terceirizado PRIMARY KEY (ideTerceirizado);

CREATE TABLE PostoTrabalho (
    idePostoTraballho UniqueIdentifier not null,
    nomPostoTrabalho varchar(60),
    qtdPostoTrabalho int,
    ideContrato UniqueIdentifier not null
);
 
ALTER TABLE PostoTrabalho ADD CONSTRAINT PK_PostoTrabalho PRIMARY KEY (idePostoTraballho);

CREATE TABLE Ocupa (
    ideOcupa UniqueIdentifier not null,
    ideTerceirizado UniqueIdentifier not null,
    idePostoTrabalho UniqueIdentifier not null,
    codSituacao varchar(1) not null,
    datInicio datetime not null,
    datFim datetime
);
 
ALTER TABLE Ocupa ADD CONSTRAINT PK_Ocupa PRIMARY KEY (ideOcupa);

CREATE TABLE Contrato (
    ideContrato UniqueIdentifier not null,
    ideOrgao UniqueIdentifier not null,
    ideFornecedor UniqueIdentifier not null,
    anoContrato int not null,
    numContrato int not null,
    datInicioVigencia datetime not null,
    datFimFigencia datetime not null,
    datFimVigenciaOriginal datetime not null
);
 
ALTER TABLE Contrato ADD CONSTRAINT PK_Contrato PRIMARY KEY (ideContrato);

CREATE TABLE Orgao (
    IdeOrgao UniqueIdentifier not null,
    nomOrgao varchar(128) not null,
    numCNPJ varchar(14) not null
);
 
alter table Orgao ADD cONSTRAINT PK_Orgao PRIMARY KEY (IdeOrgao);
 
ALTER TABLE Orgao ADD CONSTRAINT UK_Orgao_CNPJ UNIQUE (numCNPJ);

CREATE TABLE Repactuacao (
    ideRepactuacao UniqueIdentifier not null,
    ideContrato UniqueIdentifier not null,
    datAprovacao datetime not null,
    desAprovacao varchar(300) not null
);
 
ALTER TABLE Repactuacao ADD CONSTRAINT PK_Repactuacao PRIMARY KEY (ideRepactuacao);

CREATE TABLE QuantidadePostoTrabaho (
    ideQuantidadePostoTrabalho UniqueIdentifier not null,
    datReferencia datetime not null,
    desJustificativa varchar(300) not null,
    qtdPostoTrabalho int not null,
    ideRepactuacao UniqueIdentifier not null,
    idePostoTrabalho UniqueIdentifier not null
);
 
ALTER TABLE QuantidadePostoTrabaho ADD CONSTRAINT PK_QuantidadePostoTrabalho PRIMARY KEY (ideQuantidadePostoTrabalho);

CREATE TABLE Fornecedor (
    ideFornecedor UniqueIdentifier not null,
    nomForecedor VARCHAR(60) not null,
    numCNPJ VARCHAR(14) not null
);
 
ALTER TABLE Fornecedor ADD CONSTRAINT PK_Fornecedor PRIMARY KEY (ideFornecedor);
 
ALTER TABLE Fornecedor ADD CONSTRAINT UK_Fornecedor_CNPJ UNIQUE (numCNPJ);

CREATE TABLE Evento (
    ideEvento UniqueIdentifier not null,
    ideOcupa UniqueIdentifier not null,
    ideTipoEvento UniqueIdentifier not null,
    datInicio datetime not null,
    datFim datetime not null,
    codSituacaoEvento varchar(1) not null
);
 
ALTER TABLE Evento ADD CONSTRAINT PK_Evento PRIMARY KEY (ideEvento);

CREATE TABLE TipoEvento (
    ideTipoEvento UniqueIdentifier not null,
    nomTipoEvento VARCHAR(18) not null
);
 
ALTER TABLE TipoEvento ADD CONSTRAINT PK_TipoEvento PRIMARY KEY (ideTipoEvento);

CREATE TABLE Substituicao (
    ideSubstituicao UniqueIdentifier not null,
    ideEvento UniqueIdentifier not null,
    ideOcupa UniqueIdentifier not null,
    datInicio datetime not null,
    datFim datetime not null
);
 
ALTER TABLE Substituicao ADD CONSTRAINT PK_Substituicao PRIMARY KEY (ideSubstituicao);
 
ALTER TABLE PostoTrabalho ADD CONSTRAINT FK_PostoTrabalho_Contrato
    FOREIGN KEY (ideContrato)
    REFERENCES Contrato (ideContrato);
 
ALTER TABLE Ocupa ADD CONSTRAINT FR_Ocupa_Terceirizado
    FOREIGN KEY (ideTerceirizado)
    REFERENCES Terceirizado (ideTerceirizado);
 
ALTER TABLE Ocupa ADD CONSTRAINT FK_Ocupa_PostoTrabalho
    FOREIGN KEY (idePostoTrabalho)
    REFERENCES PostoTrabalho (idePostoTraballho);
 
ALTER TABLE Contrato ADD CONSTRAINT FK_Contrato_Orgao
    FOREIGN KEY (ideOrgao)
    REFERENCES Orgao (IdeOrgao);
 
ALTER TABLE Contrato ADD CONSTRAINT FK_Contrato_Fornecedor
    FOREIGN KEY (ideFornecedor)
    REFERENCES Fornecedor (ideFornecedor);
 
ALTER TABLE Repactuacao ADD CONSTRAINT Fk_Repactuacao_Contrato
    FOREIGN KEY (ideContrato)
    REFERENCES Contrato (ideContrato);
 
ALTER TABLE QuantidadePostoTrabaho ADD CONSTRAINT FK_QuantidadePostoTrabalho_Repactuacao
    FOREIGN KEY (ideRepactuacao)
    REFERENCES Repactuacao (ideRepactuacao);
 
ALTER TABLE QuantidadePostoTrabaho ADD CONSTRAINT FK_QuantidadePostoTrabalho_PostoTrabalho
    FOREIGN KEY (idePostoTrabalho)
    REFERENCES PostoTrabalho (idePostoTraballho);
 
ALTER TABLE Evento ADD CONSTRAINT FK_Evento_TipoEvento
    FOREIGN KEY (ideTipoEvento)
    REFERENCES TipoEvento (ideTipoEvento);
 
ALTER TABLE Evento ADD CONSTRAINT FK_Evento_Ocupa
    FOREIGN KEY (ideOcupa)
    REFERENCES Ocupa (ideOcupa);
 
ALTER TABLE Substituicao ADD CONSTRAINT FK_Substituicao_Ocupa
    FOREIGN KEY (ideOcupa)
    REFERENCES Ocupa (ideOcupa);
 
ALTER TABLE Substituicao ADD CONSTRAINT FK_Substituicao_Evento
    FOREIGN KEY (ideEvento)
    REFERENCES Evento (ideEvento);

