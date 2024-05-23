DROP TABLE IF EXISTS public.Users CASCADE;
DROP TABLE IF EXISTS public.Projects CASCADE;
DROP TABLE IF EXISTS public.Participants CASCADE;
DROP TABLE IF EXISTS public.Tasks CASCADE;
DROP TABLE IF EXISTS public.Projecttrees CASCADE;
DROP TABLE IF EXISTS public.Invites CASCADE;
DROP TABLE IF EXISTS public.Edges CASCADE;


CREATE TABLE IF NOT EXISTS public.Users (
    userid UUID NOT NULL,
    name VARCHAR(128) NOT NULL,
    login VARCHAR(64) NOT NULL,
    passwordhash VARCHAR(255) NOT NULL,
    PRIMARY KEY (userid),
    UNIQUE (userid, login)
);

CREATE TABLE IF NOT EXISTS public.Projects (
    projectid UUID NOT NULL,
    projectname VARCHAR(128) NOT NULL,
    owner UUID NOT NULL,
    PRIMARY KEY (projectid),
    UNIQUE (projectid)
);

CREATE TABLE IF NOT EXISTS public.Participants (
    projectid UUID NOT NULL,
    userid UUID NOT NULL,
    admin BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS public.Tasks (
    taskid UUID NOT NULL,
    projectid UUID NOT NULL,
    taskname VARCHAR(128) NOT NULL,
    executor UUID,
    datestart DATE NOT NULL,
    dateend DATE NOT NULL,
	iscomplete BOOLEAN NOT NULL default false,
    PRIMARY KEY (taskid),
    UNIQUE (taskid)
);

CREATE TABLE IF NOT EXISTS public.Edges (
	projectid UUID NOT NULL,
	taskid UUID NOT NULL,
	edgefrom varchar(64),
	edgeto varchar(64)
);

CREATE TABLE IF NOT EXISTS public.Invites (
    projectid UUID NOT NULL,
    code VARCHAR(255) NOT NULL,
    created DATE NOT NULL
);

ALTER TABLE Projects ADD FOREIGN KEY (owner) REFERENCES public.Users(userid) ON DELETE CASCADE;
ALTER TABLE Participants ADD FOREIGN KEY (userid) REFERENCES public.Users(userid) ON DELETE CASCADE;
ALTER TABLE Participants ADD FOREIGN KEY (projectid) REFERENCES public.Projects(projectid) ON DELETE CASCADE;
ALTER TABLE Tasks ADD FOREIGN KEY (executor) REFERENCES public.Users(userid) ON DELETE CASCADE;
ALTER TABLE Tasks ADD FOREIGN KEY (projectid) REFERENCES public.Projects(projectid) ON DELETE CASCADE;
ALTER TABLE Invites ADD FOREIGN KEY (projectid) REFERENCES public.Projects(projectid) ON DELETE CASCADE;
ALTER TABLE Edges ADD FOREIGN KEY (taskid) REFERENCES public.Tasks(taskid) ON DELETE CASCADE;
