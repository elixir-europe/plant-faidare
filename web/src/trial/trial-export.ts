export function initializeTrialExport(options: { contextPath: string; trialDbId: string; }) {
    let selectedLevelCode: string;

    document.querySelectorAll<HTMLButtonElement>('button[data-level-code]').forEach(button => {
        button.addEventListener('click', () => selectedLevelCode = button.getAttribute('data-level-code')!);
    });

    const exportForm = document.querySelector<HTMLFormElement>('#export-form')!;

    ['#season-names', '#study-locations', '#observation-variable-names'].forEach(checkListId => {
       exportForm.querySelector<HTMLButtonElement>(`${checkListId} .check-all`)!.addEventListener('click', () => {
           Array.from(exportForm.querySelectorAll<HTMLInputElement>(`${checkListId} input`)).forEach(checkbox => checkbox.checked = true);
       });
        exportForm.querySelector<HTMLButtonElement>(`${checkListId} .uncheck-all`)!.addEventListener('click', () => {
            Array.from(exportForm.querySelectorAll<HTMLInputElement>(`${checkListId} input`)).forEach(checkbox => checkbox.checked = false);
        })
    });

    exportForm.addEventListener('submit', async event => {
        event.preventDefault();
        const seasonNames = Array.from(exportForm.querySelectorAll<HTMLInputElement>('#season-names input:checked')).map(
            el => el.value
        );
        const studyLocations = Array.from(exportForm.querySelectorAll<HTMLInputElement>('#study-locations input:checked')).map(
            el => el.value
        );
        const observationVariableNames = Array.from(exportForm.querySelectorAll<HTMLInputElement>('#observation-variable-names input:checked')).map(
            el => el.value
        );
        const exportFormat = exportForm.querySelector<HTMLInputElement>('input[name=exportFormat]:checked')!.value;

        const submitButton = document.querySelector<HTMLButtonElement>('#export-submit')!;
        const progress = document.querySelector('#export-in-progress')!;
        const error = document.querySelector('#export-error')!;
        const spinner = document.querySelector('#export-spinner')!;
        submitButton.disabled = true;
        error.classList.add('d-none');
        spinner.classList.remove('d-none');
        progress.classList.remove('d-none');

        const command = {
            trialDbId: options.trialDbId,
            observationLevelCode: selectedLevelCode,
            studyLocations: studyLocations,
            seasonNames: seasonNames,
            observationVariableNames: observationVariableNames,
            format: exportFormat
        }

        try {
            const response = await fetch(`${options.contextPath}/observation-units/exports`, {
                method: 'POST',
                body: JSON.stringify(command),
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                error.classList.remove('d-none');
            } else {
                const blob = await response.blob();
                const downloadUrl = URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = downloadUrl;
                link.download = `observations.${exportFormat === 'EXCEL' ? 'xlsx' : 'csv' }`;
                link.click();

                URL.revokeObjectURL(downloadUrl);
            }
        } catch (_error) {
            error.classList.remove('d-none');
        }
        finally {
            submitButton.disabled = false;
            spinner.classList.add('d-none');
            progress.classList.add('d-none');
        }
    });
}
