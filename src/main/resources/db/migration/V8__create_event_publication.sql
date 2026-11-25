CREATE TABLE public.event_publication (
    id UUID NOT NULL,
    completion_attempts INTEGER NOT NULL,
    completion_date TIMESTAMP WITH TIME ZONE,
    event_type VARCHAR(255) NOT NULL,
    last_resubmission_date TIMESTAMP WITH TIME ZONE,
    listener_id VARCHAR(255) NOT NULL,
    publication_date TIMESTAMP WITH TIME ZONE NOT NULL,
    serialized_event VARCHAR(255) NOT NULL,
    status VARCHAR(255),
    CONSTRAINT pk_event_publication PRIMARY KEY (id)
);

CREATE INDEX event_publication_by_completion_date_idx
    ON public.event_publication (completion_date);

CREATE INDEX event_publication_by_listener_and_event_idx
    ON public.event_publication (listener_id, serialized_event)
    WHERE completion_date IS NULL;

CREATE INDEX event_publication_by_status_idx
    ON public.event_publication (status);

CREATE TABLE public.event_publication_archive (
    id UUID NOT NULL,
    completion_attempts INTEGER NOT NULL,
    completion_date TIMESTAMP WITH TIME ZONE,
    event_type VARCHAR(255) NOT NULL,
    last_resubmission_date TIMESTAMP WITH TIME ZONE,
    listener_id VARCHAR(255) NOT NULL,
    publication_date TIMESTAMP WITH TIME ZONE NOT NULL,
    serialized_event VARCHAR(255) NOT NULL,
    status VARCHAR(255),
    CONSTRAINT pk_event_publication_archive PRIMARY KEY (id)
);

CREATE INDEX event_publication_archive_by_completion_date_idx
    ON public.event_publication_archive (completion_date);
