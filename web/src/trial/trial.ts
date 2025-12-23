import Modal from 'bootstrap/js/dist/modal';

/**
 * An export job, matching the backend response for the endpoints allowing to create and get jobs.
 */
export interface Job {
  id: string;
  status: 'RUNNING' | 'DONE' | 'FAILED';
}

/**
 * Function called by the trial page when it loads.
 * It binds a listener to the N export dropdown buttons in order to display the export modal
 * defined in the page.
 * This modal contains a form, and the submission of that form is also handled by a listener
 * added by this function.
 * When submitted, a new export job is started on the backend (because exporting observation
 * units can be very long), and a new tab is opened in order to monitor the progress of this job.
 * Finally, this function also binds listeners for the Check All / Uncheck all buttons
 * inside the form.
 */
export function initializeTrial(options: { contextPath: string; trialDbId: string }) {
  // when opening the modal using one of the N export dropdown buttons, this variable is set
  // to the level code corresponding to the clicked button
  let selectedLevelCode: string;

  document.querySelectorAll<HTMLButtonElement>('button[data-level-code]').forEach(button => {
    button.addEventListener('click', () => (selectedLevelCode = button.getAttribute('data-level-code')!));
  });

  // bind click listeners to the three Check all / Uncheck all buttons of the form
  const exportForm = document.querySelector<HTMLFormElement>('#export-form')!;
  ['#season-names', '#study-locations', '#observation-variable-names'].forEach(checkListId => {
    exportForm.querySelector<HTMLButtonElement>(`${checkListId} .check-all`)!.addEventListener('click', () => {
      Array.from(exportForm.querySelectorAll<HTMLInputElement>(`${checkListId} input`)).forEach(checkbox => (checkbox.checked = true));
    });
    exportForm.querySelector<HTMLButtonElement>(`${checkListId} .uncheck-all`)!.addEventListener('click', () => {
      Array.from(exportForm.querySelectorAll<HTMLInputElement>(`${checkListId} input`)).forEach(checkbox => (checkbox.checked = false));
    });
  });

  // handle the export form submission
  exportForm.addEventListener('submit', async event => {
    event.preventDefault();
    const seasonNames = Array.from(exportForm.querySelectorAll<HTMLInputElement>('#season-names input:checked')).map(el => el.value);
    const studyLocations = Array.from(exportForm.querySelectorAll<HTMLInputElement>('#study-locations input:checked')).map(el => el.value);
    const observationVariableNames = Array.from(
      exportForm.querySelectorAll<HTMLInputElement>('#observation-variable-names input:checked')
    ).map(el => el.value);
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
    };

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
        const job = (await response.json()) as Job;
        window.open(`${options.contextPath}/trials/${options.trialDbId}/exports/${job.id}`);
        Modal.getInstance(document.querySelector('#exportModal')!)!.hide();
      }
    } catch (_error) {
      error.classList.remove('d-none');
    } finally {
      submitButton.disabled = false;
      spinner.classList.add('d-none');
      progress.classList.add('d-none');
    }
  });
}
